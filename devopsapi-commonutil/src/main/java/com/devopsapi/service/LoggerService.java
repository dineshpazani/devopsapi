package com.devopsapi.service;

import org.aspectj.lang.ProceedingJoinPoint;

public interface LoggerService {
	
	Object infoLogger(ProceedingJoinPoint pjp) throws Throwable;

}
