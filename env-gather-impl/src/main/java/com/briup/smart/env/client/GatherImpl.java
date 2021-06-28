package com.briup.smart.env.client;
/**
 * 作用：采集数据：对data-file文件中的数据进行处理
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.util.BackupImpl;

public class GatherImpl implements Gather{
	BackupImpl bi=new BackupImpl();
	
	//保存备份文件的路径名
	String filePath="src/main/resources/fileLength.txt";
	
	long length=0L;
	
	@Override
	public Collection<Environment> gather() throws Exception {
		// TODO Auto-generated method stub
		File file = new File("src/main/resources/data-file-simple");
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		//获取文件字节数
		length = file.length()+1;
		//获取备份文件的字节数
		Object load = bi.load(filePath, false);
		if(load!=null) {
			Long l=(Long) load;
			in.skip(l);
		}
		
		
		Collection<Environment> a= new ArrayList<Environment>();
		
		
		String s=null;
//		String[] s2=null;
		while((s=in.readLine())!=null) {
			String[] s2 = s.split("[|]");
			String srcId=s2[0];
			String desId=s2[1];
			String devId=s2[2];
			String cmd=s2[5];
			String name1=null;
			String name2=null;
			String sersorAddress=s2[3];
			int count=Integer.parseInt(s2[4]);
			int Address=Integer.parseInt(s2[3]);
			int status=Integer.parseInt(s2[7]);
			Timestamp gather_date = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Double.parseDouble(s2[8])));
			float datalast=0f;
			if(Address==16) {
				name1="温度";
				name2="湿度";
				//获取温度和湿度
				String tem = s2[6].substring(0,4);
				float data1=(float)Long.parseLong(tem, 16);
				datalast=(data1*(0.00268127F))-46.85F;
				String dam = s2[6].substring(4,8);
				float data2=(float)Long.parseLong(dam, 16);
				float datadam=(data2*0.00190735F)-6;
				
				Environment en2 = new Environment(name2, srcId, desId, devId, sersorAddress, count, cmd, status, datadam, gather_date);

				a.add(en2);
			}else if(Address==256) {
				name1="光照强度";
				String ill = s2[6].substring(0,4);
				datalast=(float)Long.parseLong(ill, 16);
			}else {
				name1="二氧化碳";
				String co2 = s2[6].substring(0,4);
				datalast=(float)Long.parseLong(co2, 16);
			}
			Environment en1 = new Environment(name1, srcId, desId, devId, sersorAddress, count, cmd, status, datalast, gather_date);
			a.add(en1);
		}
		//将文件长度保存在备份文件中
		bi.store(filePath, length, false);
		
		
		return a;
	}

}
