package com.ichee.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.ichee.dao.interfaces.UserDAO;
import com.ichee.entities.User;
import com.ichee.setting.PasswordSalt;

@Repository()
public class UserDAOImpl implements UserDAO
{
	@Autowired
	private DataSource dataSource;

	@Override
	public int insert(User user)
	{
		String username = user.getUsername();
		String password = user.getPassword();
		String email = user.getEmail();
		Timestamp ts = new Timestamp(new Date().getTime());
		
		MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
		String hashed_password = encoder.encodePassword(password, PasswordSalt.salt);
		
		Connection conn = null;
		PreparedStatement ps = null;
		int line = 0;
		try {
			conn = dataSource.getConnection();
			String sql = "INSERT INTO users(username, password, email, created_at, updated_at) VALUES(?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, hashed_password);
			ps.setString(3, email);
			ps.setTimestamp(4, ts);
			ps.setTimestamp(5, ts);
			line = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return line;
	}

	@Override
	public User find(Integer id)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "SELECT * FROM users WHERE uid = ?";
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id.intValue());
			rs = ps.executeQuery();
			
			if (rs.next()) {
				Integer uid = new Integer(rs.getInt(1));
				String username = rs.getString(2);
				String password = rs.getString(3);
				String email = rs.getString(4);
				Timestamp created_at = rs.getTimestamp(5);
				Timestamp updated_at = rs.getTimestamp(6);
				Timestamp deleted_at = rs.getTimestamp(7);
				User user = new User();
				user.setUid(uid);
				user.setUsername(username);
				user.setPassword(password);
				user.setEmail(email);
				user.setCreated_at(created_at);
				user.setUpdated_at(updated_at);
				user.setDeleted_at(deleted_at);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
