package com.briup.smart.env.server;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.briup.smart.env.client.GatherImpl;
import com.briup.smart.env.entity.Environment;

class DBStoreImplTest {

	@Test
	void test() {
		GatherImpl gatherImpl = new GatherImpl();
		
		DBStoreImpl dbStoreImpl = new DBStoreImpl();
		try {
			Collection<Environment> gather = gatherImpl.gather();
			dbStoreImpl.saveDB(gather);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
