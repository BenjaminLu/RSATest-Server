package com.ichee.dao.interfaces;

import com.ichee.entities.User;

public interface UserDAO
{
	public int insert(User user);
    public User find(Integer id);
}
