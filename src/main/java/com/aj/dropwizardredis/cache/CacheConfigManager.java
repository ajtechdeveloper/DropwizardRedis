package com.aj.dropwizardredis.cache;

import com.aj.dropwizardredis.domain.Student;
import com.aj.dropwizardredis.service.StudentService;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheConfigManager {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfigManager.class);

    private static CacheConfigManager cacheConfigManager = new CacheConfigManager();

    public static CacheConfigManager getInstance() {
        return cacheConfigManager;
    }

    //Logic For Student Cache
    public Student getStudentDataFromCache(String key, StudentService studentService,
                                           RMap<String, Student> map) {
        try {
            Student student;
            if(CollectionUtils.isEmpty(map.keySet())){
                student = studentService.getFromDatabase(key);
                map.put(key, student);
            }
            else{
                if(map.containsKey(key)){
                    student = map.get(key);
                }
                else{
                    student = studentService.getFromDatabase(key);
                    map.put(key, student);
                }
            }
            logger.info("All Entries in Student map: {}",map.readAllEntrySet());
            return student;
        } catch (Exception e) {
            logger.error("Error Retrieving Elements from the Student Cache"
                    + e.getMessage());
        }
        return null;
    }

}
