package com.briup.smart.env.util;

import org.apache.log4j.Logger;

public class LogImpl implements Log{
	Logger log=Logger.getRootLogger();

	@Override
	public void debug(String message) {
		// TODO Auto-generated method stub
		log.debug(message);
		
	}

	@Override
	public void info(String message) {
		// TODO Auto-generated method stub
		log.info(message);
		
	}

	@Override
	public void warn(String message) {
		// TODO Auto-generated method stub
		log.warn(message);
		
		
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		log.error(message);
		
		
	}

	@Override
	public void fatal(String message) {
		// TODO Auto-generated method stub
		log.fatal(message);
		
		
	}

}
