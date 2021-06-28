package com.briup.smart.env.main;

import java.util.Collection;

import com.briup.smart.env.client.ClientImpl;
import com.briup.smart.env.client.GatherImpl;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.DBStoreImpl;

//客户端入口类
public class ClientMain {
	public static void main(String[] args) {
		GatherImpl gatherImpl = new GatherImpl();
		Collection<Environment> gather;
		try {
			gather = gatherImpl.gather();
			ClientImpl clientImpl = new ClientImpl();
			clientImpl.send(gather);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
