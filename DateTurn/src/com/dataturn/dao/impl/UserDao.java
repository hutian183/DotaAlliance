package com.dataturn.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dataturn.bean.User;
import com.dataturn.dao.IUserDao;
import com.dataturn.util.JDBCUtil;

public class UserDao implements IUserDao {
	private Connection con=JDBCUtil.getConnection();
	
	private void setUser(User user,ResultSet resultSet) throws SQLException{
		if(resultSet.first()){
			user.setUserId(resultSet.getString("user_id"));
			user.setUserName(resultSet.getString("user_name"));
			user.setPassword(resultSet.getString("password"));
			user.setIp(resultSet.getString("ip"));
			user.setPort(resultSet.getString("port"));
			user.setStatus(resultSet.getString("status"));
		}
	}
	@Override
	public void addUser(User user) {
		String sql="insert into user(user_id,user_name,password,ip,port)" +
				" value(?,?,?,?,?)";
		try {
			PreparedStatement 	sm=con.prepareStatement(sql);
			sm.setString(1, user.getUserId());
			sm.setString(2, user.getUserName());
			sm.setString(3, user.getPassword());
			sm.setString(4, user.getIp());
			sm.setString(5, user.getPort());
			sm.execute();
			sm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getUserById(String userId) throws SQLException {
		String sql="select * from user where user_id=?";
		User user=new User();
		PreparedStatement 	sm=con.prepareStatement(sql);
		sm.setString(1, userId);
		ResultSet resultSet= sm.executeQuery();
		setUser(user, resultSet);
		sm.close();
		return user;
	}
	
	
	@Override
	public User getUserByName(String userName) throws SQLException {
		String sql="select * from user where user_name=?";
		User user=new User();
		PreparedStatement 	sm=con.prepareStatement(sql);
		sm.setString(1, userName);
		ResultSet resultSet= sm.executeQuery();
		setUser(user, resultSet);
		sm.close();
		return user;
	}
	@Override
	public void updateUser(User user) throws SQLException {
		String sql="update user set user_name=?,password=?,ip=?,port=?,status=?" +
				" where user_id=?";
		PreparedStatement 	sm=con.prepareStatement(sql);
		sm.setString(1, user.getUserName());
		sm.setString(2, user.getPassword());
		sm.setString(3, user.getIp());
		sm.setString(4, user.getPort());
		sm.setString(5, user.getStatus());
		sm.setString(6, user.getUserId());
		sm.execute();
		sm.close();
	}
	@Override
	public void updateUserField(User user) throws SQLException {
		String sql="update user set ip=?,port=?,status=? where user_name=?";
		PreparedStatement 	sm=con.prepareStatement(sql);
		sm.setString(1, user.getIp());
		sm.setString(2, user.getPort());
		sm.setString(3, user.getStatus());
		sm.setString(4, user.getUserName());
		sm.execute();
		sm.close();
	}
}
