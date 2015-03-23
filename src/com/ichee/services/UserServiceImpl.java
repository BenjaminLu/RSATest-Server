package com.ichee.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichee.dao.interfaces.UserDAO;
import com.ichee.entities.User;
import com.ichee.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public int insert(User user)
	{
		int line = userDAO.insert(user);
		return line;
	}

	@Override
	public User find(Integer id)
	{
		User user = userDAO.find(id);
		return user;
	}

}
