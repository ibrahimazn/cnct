package com.appfiss.account.audit;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Logging handler.
 *
 */
@Aspect
@Component
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LoggingHandler {

	/** Logger attribute. */
	private static final Logger log = LoggerFactory.getLogger(LoggingHandler.class);

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void allMethod() {
	}

	@Pointcut("execution(public * *(..))")
	protected void loggingPublicOperation() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void loggingAllOperation() {
	}

	@Pointcut("within(com.appfiss.account.*)")
	private void logAnyFunctionWithinResource() {
	}

	// before -> Any resource annotated with @Controller annotation
	// and all method and function taking HttpServletRequest as first parameter
	@Before("controller() && allMethod() && args(..,request)")
	public void logBefore(JoinPoint joinPoint, HttpServletRequest request) {

		log.debug("Entering in Method :  " + joinPoint.getSignature().getName());
		log.debug("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
		log.debug("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
		log.debug("Target class : " + joinPoint.getTarget().getClass().getName());

		if (null != request) {
			log.debug("Start Header Section of request ");
			log.debug("Method Type : " + request.getMethod());
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				String headerValue = request.getHeader(headerName);
				log.debug("Header Name: " + headerName + " Header Value : " + headerValue);
			}
			log.debug("Request Path info :" + request.getServletPath());
			log.debug("End Header Section of request ");
		}
	}

	// After -> All method within resource annotated with @Controller annotation
	// and return a value
	@AfterReturning(pointcut = "controller() && allMethod()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
		long start = System.currentTimeMillis();
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String type = ((MethodSignature) joinPoint.getSignature()).getMethod().getGenericReturnType().getTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			log.debug("args " + joinPoint.getArgs());
			long elapsedTime = System.currentTimeMillis() - start;
			log.debug("Method " + className + ".this.resourceType = resourceType;" + methodName + " ()"
					+ " execution time : " + elapsedTime + " ms" + result);
		} catch (Exception e) {
			log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
					+ joinPoint.getSignature().getName() + "()");
		}
	}

	// After -> Any method within resource annotated with @Controller annotation
	// throws an exception ...Log it
	@AfterThrowing(pointcut = "controller() && allMethod()", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint) {
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String type = ((MethodSignature) joinPoint.getSignature()).getMethod().getGenericReturnType().getTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
		} catch (Exception e) {
			log.debug("Throws logger failed" + e.getCause());
		}
	}

	// Around -> Any method within resource annotated with @Controller
	// annotation
	@Around("controller() && allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object result = null;
		try {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String type = ((MethodSignature) joinPoint.getSignature()).getMethod().getGenericReturnType().getTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			log.debug("args " + joinPoint.getArgs());
			long elapsedTime = System.currentTimeMillis() - start;
			log.debug("Method " + className + ".this.resourceType = resourceType;" + methodName + " ()"
					+ " execution time : " + elapsedTime + " ms" + result);
			result = joinPoint.proceed();

		} catch (IllegalArgumentException e) {
			log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
					+ joinPoint.getSignature().getName() + "()");
			result = joinPoint.proceed();
			return result;
		}
		return result;
	}

	/**
	 * Gets the value from object.
	 *
	 * @param result the result
	 * @return the value
	 */
	private String getValue(Object result) {
		String returnValue = null;
		if (null != result) {
			if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
				returnValue = ReflectionToStringBuilder.toString(result);
			} else {
				returnValue = result.toString();
			}
		}
		return returnValue;
	}

}
