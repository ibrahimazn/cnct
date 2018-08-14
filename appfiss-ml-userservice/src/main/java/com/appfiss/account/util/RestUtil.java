package com.appfiss.account.util;


import org.springframework.http.HttpStatus;

/**
 * Utility class for REST call to check whether client has error or not while
 * accessing from inside service.
 */
public class RestUtil {

	/**
	 * Checks if http status is error or not. if error it will return boolean
	 * true value.
	 *
	 * @param status
	 *            the status
	 * @return true, if is error
	 */
	public static boolean isError(HttpStatus status) {
		HttpStatus.Series series = status.series();
		return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
	}
}

