package com.aj.dropwizardredis;

import com.aj.dropwizardredis.domain.Student;
import com.aj.dropwizardredis.resource.StudentResource;
import com.aj.dropwizardredis.resource.DropwizardRedisHealthCheckResource;
import com.aj.dropwizardredis.resource.PingResource;
import com.aj.dropwizardredis.service.StudentService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class DropwizardRedisApplication extends Application<DropwizardRedisConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(DropwizardRedisApplication.class);

	public static void main(String[] args) throws Exception {
		new DropwizardRedisApplication().run("server", args[0]);
	}

    @Override
    public void initialize(Bootstrap<DropwizardRedisConfiguration> b) {
    }

	@Override
	public void run(DropwizardRedisConfiguration config, Environment env)
			throws Exception {
        //Connect to 127.0.0.1:6379 by default
	    RedissonClient redissonClient = Redisson.create();
	    RedissonManaged redissonManaged = new RedissonManaged(redissonClient);
        env.lifecycle().manage(redissonManaged);
        RMap<String, Student> studentMap = redissonClient.getMap(config.getStudentCacheKey());
        //Cache will expire after 30 minutes
        studentMap.expire(30, TimeUnit.MINUTES);
	    StudentService studentService = new StudentService();
	    logger.info("Registering RESTful API resources");
		env.jersey().register(new PingResource());
        env.jersey().register(new StudentResource(studentService,studentMap));
		env.healthChecks().register("DropwizardCacheHealthCheck",
				new DropwizardRedisHealthCheckResource(config));
	}
}
