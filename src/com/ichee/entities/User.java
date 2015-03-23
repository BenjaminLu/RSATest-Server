package com.ichee.entities;

import java.sql.Timestamp;

public class User
{
	private Integer uid;
	private String username;
	private String password;
	private String email;
	private Timestamp created_at;
	private Timestamp updated_at;
	private Timestamp deleted_at;
	

	public Integer getUid()
	{
		return uid;
	}
	public void setUid(Integer uid)
	{
		this.uid = uid;
	}
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public Timestamp getCreated_at()
	{
		return created_at;
	}
	public void setCreated_at(Timestamp created_at)
	{
		this.created_at = created_at;
	}
	public Timestamp getUpdated_at()
	{
		return updated_at;
	}
	public void setUpdated_at(Timestamp updated_at)
	{
		this.updated_at = updated_at;
	}
	public Timestamp getDeleted_at()
	{
		return deleted_at;
	}
	public void setDeleted_at(Timestamp deleted_at)
	{
		this.deleted_at = deleted_at;
	}
	
}
