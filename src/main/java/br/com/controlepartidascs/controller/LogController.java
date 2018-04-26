package br.com.controlepartidascs.controller;

import org.apache.log4j.Logger;

public class LogController {
	private static Logger logger = Logger.getLogger(LogController.class);

	public static void log(String log) {
		logger.info(log);
	}

	public static void logError(String log) {
		logger.error(log);
	}

	public static void logWarning(String log) {
		logger.warn(log);
	}

	public static void logDebug(String log) {
		logger.debug(log);
	}
	
	public static void logFatal(String log){
	      logger.fatal(log);
	   }
}
