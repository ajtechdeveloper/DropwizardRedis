package com.aj.dropwizardredis.cache;

import com.aj.dropwizardredis.domain.Student;
import com.aj.dropwizardredis.service.StudentService;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class CacheConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfigManager.class);

    private static CacheConfigManager cacheConfigManager = new CacheConfigManager();

    public static CacheConfigManager getInstance() {
        return cacheConfigManager;
    }

    //Logic For Student Cache
    public Student getStudentDataFromCache(String key, StudentService studentService,
                                           RMapCache<String, Student> studentRMapCache) {
        try {
            Student student;
            if(CollectionUtils.isEmpty(studentRMapCache.keySet())){
                student = studentService.getFromDatabase(key);
                //Cache will expire after 30 minutes
                studentRMapCache.put(key, student, 30, TimeUnit.MINUTES);
            }
            else{
                if(studentRMapCache.containsKey(key)){
                    student = studentRMapCache.get(key);
                }
                else{
                    student = studentService.getFromDatabase(key);
                    //Cache will expire after 30 minutes
                    studentRMapCache.put(key, student,30, TimeUnit.MINUTES);
                }
            }
            logger.info("All Entries in Student map: {}",studentRMapCache.readAllEntrySet());
            return student;
        } catch (Exception e) {
            logger.error("Error Retrieving Elements from the Student Cache"
                    + e.getMessage());
            return null;
        }
    }
}
