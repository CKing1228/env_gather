package com.briup.smart.env.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.function.Function;
import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class JdbcUtils {
	private static DataSource dataSource;
	static {
		try {
			Properties properties = new Properties();
			File file = new File("src/main/resources/oracle.properties");
			InputStream is = new FileInputStream(file);
			properties.load(is);
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	public static void close(ResultSet rs,Statement stmt,Connection conn) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(stmt!=null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(Statement stmt,Connection conn) {
		close(null,stmt,conn);
	}
	public static int executeUpdate(String sql) {
		int rows = 0;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rows = stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(stmt,conn);
		}
		return rows;
	}
	public static <T> T queryForObject(String sql,Function<ResultSet,T> f) {
		T t = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			t = f.apply(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs,stmt,conn);
		}
		return t;
	}
}