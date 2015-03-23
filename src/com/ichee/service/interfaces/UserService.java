package com.ichee.service.interfaces;

import com.ichee.entities.User;

public interface UserService
{
	public int insert(User user);
    public User find(Integer id);
}
