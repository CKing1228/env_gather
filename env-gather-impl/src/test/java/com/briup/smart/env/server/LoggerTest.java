package com.briup.smart.env.server;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

public class LoggerTest {
	@Test
	public void logger1() {
		//获取日志记录器
		//方式1
		Logger rootLogger = Logger.getRootLogger();
		
		//方式2
		Logger logger = Logger.getLogger("logger");
		
		//方式3
		Logger logger2 = Logger.getLogger(LoggerTest.class);
		
		rootLogger.debug("debug");
		rootLogger.info("info");
		rootLogger.warn("warn");
		rootLogger.error("error");
		rootLogger.fatal("fatal");
		
		
	}

}
