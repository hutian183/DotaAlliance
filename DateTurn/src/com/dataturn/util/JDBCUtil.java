package com.dataturn.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class JDBCUtil {
	private static DataSource dataSource;
	
	public static void initDataSource(){
		FileInputStream is = null;
		Properties properties = new Properties();
		
		String driverClassName = null;
		String url = null;
		String username = null;
		String password = null;
		
		int initialSize = 0;
		int minIdle = 0;
		int maxIdle = 0;
		int maxWait = 0;
		int maxActive = 0;
		
		try {
			//String path = System.getProperty("user.dir");
			URL path=JDBCUtil.class.getClassLoader().getResource("");
			String resultpath=URLDecoder.decode(path.getPath(), "UTF-8");
			is = new FileInputStream(resultpath+"dbcp.properties");
			properties.load(is);
			
			driverClassName = properties.getProperty("dbcp.driverClassName");
			url = properties.getProperty("dbcp.url");
			username = properties.getProperty("dbcp.username");
			password = properties.getProperty("dbcp.password");
			initialSize = Integer.parseInt((properties.getProperty("dbcp.initialSize").trim()));
			minIdle = Integer.parseInt((properties.getProperty("dbcp.minIdle")).trim());
			maxIdle = Integer.parseInt((properties.getProperty("dbcp.maxIdle")).trim());
			maxWait = Integer.parseInt((properties.getProperty("dbcp.maxWait")).trim());
			maxActive = Integer.parseInt((properties.getProperty("dbcp.maxActive")).trim());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BasicDataSource bds = new BasicDataSource();
		
		bds.setUrl(url);
		bds.setDriverClassName(driverClassName);
		bds.setUsername(username);
		bds.setPassword(password);
		bds.setInitialSize(initialSize);
		bds.setMaxActive(maxActive);
		bds.setMinIdle(minIdle);
		bds.setMaxIdle(maxIdle);
		bds.setMaxWait(maxWait);
		
		dataSource = bds;
	}
	
	public static Connection  getConnection()    {
        if (dataSource == null) {   
        	initDataSource();   
        }   
        Connection conn = null;   
        if (dataSource != null) {   
            try {
				conn = dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}   
        }   
        return conn;   
	}
	
}

