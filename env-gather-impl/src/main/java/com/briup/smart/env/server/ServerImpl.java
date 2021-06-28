package com.briup.smart.env.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.print.attribute.standard.Severity;

import org.apache.log4j.Logger;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

public class ServerImpl implements Server,ConfigurationAware,PropertiesAware{
	Log log;

	private DBStore dbstore;

	private int serverPort;
	@Override
	public void reciver() throws Exception {
		// TODO Auto-generated method stub

		//获取端口

		
		ServerSocket server=new ServerSocket(serverPort);

		log.info("服务器开启，等待客户端连接");
		while(true) {


			Socket client=server.accept();
			//创建线程
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					try {

						//创建对象流，进行反序列化
						ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

						Object object = ois.readObject();

						if(object instanceof Collection) {
							Collection<Environment> environments= (Collection<Environment>) object;
							log.info("SERVER:"+Thread.currentThread().getName()+"处理，成功接收数据"+environments.size()+"条");
							//调用入库模块
							dbstore.saveDB(environments);
						}
						log.info("等待下次连接");
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if(client!=null) {
							try {
								client.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
			});

			t.start();
		}




	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		serverPort = Integer.valueOf(properties.getProperty("server-port"));
	}

	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		// TODO Auto-generated method stub
		log = configuration.getLogger();
		dbstore = configuration.getDbStore();
	}

}
