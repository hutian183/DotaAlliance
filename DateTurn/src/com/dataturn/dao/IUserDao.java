package com.dataturn.dao;

import java.sql.SQLException;

import com.dataturn.bean.User;




public interface IUserDao {
	void addUser(User user);
	User getUserById(String userId) throws SQLException;
	User getUserByName(String userName) throws SQLException;
	void updateUser(User user) throws SQLException;
	void updateUserField(User user) throws SQLException;
}
