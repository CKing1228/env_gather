package com.briup.smart.env.main;

import com.briup.smart.env.server.ServerImpl;

//服务器入口类
public class ServerMain {
	
	public static void main(String[] args) {
		ServerImpl severImpl = new  ServerImpl();
		try {
			severImpl.reciver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
