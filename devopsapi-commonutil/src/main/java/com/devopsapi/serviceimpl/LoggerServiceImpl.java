package com.devopsapi.serviceimpl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.devopsapi.service.LoggerService;
import com.fasterxml.jackson.databind.ObjectMapper;


@Aspect
@Component
public class LoggerServiceImpl implements LoggerService {

	Logger log = LoggerFactory.getLogger(LoggerServiceImpl.class);
	
	@Pointcut(value="execution(* com.devopsapi.*.*.*(..) )")
	public void myPointcut() {
		
	}
	
	@Around("myPointcut()")
	public Object infoLogger(ProceedingJoinPoint pjp) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().toString();
		Object[] array = pjp.getArgs();
		log.info("method invoked " + className + " : " + methodName + "() " + "arguments : "
				+ mapper.writeValueAsString(array));
		Object object = pjp.proceed();
		log.info(className + " : " + methodName + "() " + "Response : "
				+ mapper.writeValueAsString(object));
		return object;
	}

}
