package com.briup.smart.env.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.BackupImpl;
import com.briup.smart.env.util.JdbcUtils;
import com.briup.smart.env.util.Log;
import com.briup.smart.env.util.LogImpl;

public class DBStoreImpl implements DBStore,ConfigurationAware,PropertiesAware{
	private Log log;
	private Backup bi;
	
	//备份文件的路径名
	String fileName = "src/main/resources/backup.txt";
	@Override
	public void saveDB(Collection<Environment> c) throws Exception {
		
		// TODO Auto-generated method stub
		//获取数据库连接对象
		Connection conn = JdbcUtils.getConnection();
		//设置不自动提交事务
		conn.setAutoCommit(false);
		
		PreparedStatement ps=null;
		//记录前一天的
		int yesterday=0;
		
		//批处理条数
		int sum=0;
		
		/*	Object load = bi.load(fileName, true);
			if(load!=null) {
				Collection<Environment> back=(Collection<Environment>) load;
				c.addAll(back);
			}*/
		try {
			//遍历集合
			for (Environment en : c) {
				//获取天数
				int day = en.getGather_date().toLocalDateTime().getDayOfMonth();
				//天数不相同就创建一个新的prepareStatment对象
				if(yesterday!=day) {
					yesterday=day;
					//处理前一天剩余的数据
					if(ps!=null) {
						ps.executeBatch();
						ps.clearBatch();
						ps.close();
					}
					String sql="insert into E_DETAIL_"+day+" values(?,?,?,?,?,?,?,?,?,?)";
					ps = conn.prepareStatement(sql);
				}
				//动态化设置参数
				ps.setString(1, en.getName());
				ps.setString(2, en.getSrcId());
				ps.setString(3, en.getDesId());
				ps.setString(4, en.getDevId());
				ps.setString(5, en.getSersorAddress());
				ps.setInt(6, en.getCount());
				ps.setString(7, en.getCmd());
				ps.setInt(8, en.getStatus());
				ps.setFloat(9, en.getData());
				ps.setTimestamp(10, en.getGather_date());
				
				//使用批处理
				ps.addBatch();
				sum++;
				
				//模拟入库异常
				/*if(sum==100) {
					throw new Exception("模拟入库发生异常");
				}*/
				//达到2000条处理一次
				if(sum%200==0) {
					ps.executeBatch();
					ps.clearBatch();
					
				}
				
				
			
			}
			//处理最后一天的数据
			ps.executeBatch();
			ps.clearBatch();
			conn.commit();
			
			log.info("插入完毕");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//回滚
			conn.rollback();
			//备份数据
			bi.store(fileName, c, false);
			
			e.printStackTrace();
		}finally {
			JdbcUtils.close(ps, conn);
		}
	}
	@Override
	public void init(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
