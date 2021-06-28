package com.briup.smart.env.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

public class ClientImpl implements Client,ConfigurationAware,PropertiesAware{
	private Log log;
	private String host;
	private int port;

	@Override
	public void send(Collection<Environment> c) throws Exception {
		// TODO Auto-generated method stub
		Socket client=new Socket(host, port);
		log.info("客户端"+client.getLocalPort()+"已开启");

		ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
		
		oos.writeObject(c);
		log.info("发送完毕,发送"+c.size()+"条数据");
		oos.flush();
		oos.writeObject(null);


		if(oos!=null) {
			oos.close();
		}
		if(client!=null) {
			client.close();
		}
	}

	@Override
	public void init(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		host = properties.getProperty("host");
		port = Integer.valueOf(properties.getProperty("port"));
		
	}

	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		// TODO Auto-generated method stub
		log = configuration.getLogger();
		
	}



}
