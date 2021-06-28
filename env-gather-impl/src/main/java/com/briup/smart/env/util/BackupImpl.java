package com.briup.smart.env.util;
/*
 * 备份模块
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BackupImpl implements Backup{

	@Override
	public Object load(String fileName, boolean del) throws Exception {
		// TODO Auto-generated method stub
		File file = new File(fileName);
		
		if(!file.exists()) {
			throw new Exception("文件找不到");
		}
		
		FileInputStream fis = new  FileInputStream(file);
		ObjectInputStream ois = new  ObjectInputStream(fis);
		
		//从备份文件中读取数据
		Object object = ois.readObject();
		
		//关闭防止文件被占用
		ois.close();
		//判断是否删除文件
		if(del) {
			file.delete();
		}
		
		return object;
	}

	@Override
	public void store(String fileName, Object obj, boolean append) throws Exception {
		// TODO Auto-generated method stub
		File file = new File(fileName);
		//假如文件不存在就创建
		if(!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file,append);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		
		oos.writeObject(obj);
		oos.flush();
		
		if(oos!=null) {
			oos.close();
		}
		
	}

}
