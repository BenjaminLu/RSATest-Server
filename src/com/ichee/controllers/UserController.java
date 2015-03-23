package com.ichee.controllers;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ichee.entities.User;
import com.ichee.helpers.RSA;
import com.ichee.service.interfaces.UserService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Controller
public class UserController
{
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/add", produces = { "application/json" }, method = { RequestMethod.POST })
	@ResponseBody
	public String add(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "password_check", required = true) String password_check,
			@RequestParam(value = "email", required = true) String email)
	{
		System.out.println(username);
		JSONObject object = null;
		if (password.equals(password_check)) {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
			int line = userService.insert(user);

			object = new JSONObject();
			object.put("status", true);
			object.put("line", line);
		} else {
			object = new JSONObject();
			object.put("status", false);
		}
		return object.toString();
	}

	@RequestMapping(value = "/user/{uid}", produces = { "application/json" }, method = { RequestMethod.GET })
	@ResponseBody
	public String add(@PathVariable int uid)
	{
		JSONObject object = new JSONObject();
		User user = userService.find(new Integer(uid));
		object.put("uid", user.getUid());
		object.put("username", user.getUsername());
		object.put("password", user.getPassword());
		object.put("email", user.getEmail());
		object.put("created_at", user.getCreated_at());
		object.put("updated_at", user.getUpdated_at());
		object.put("deleted_at", user.getDeleted_at());
		return object.toString();
	}

	@RequestMapping(value = "/verify", produces = { "application/json" }, method = { RequestMethod.POST })
	@ResponseBody
	public String verify(
			@RequestParam(value = "message", required = true) String message,
			@RequestParam(value = "sign", required = true) String signBase64,
			@RequestParam(value = "modulus", required = true) String modulus,
			@RequestParam(value = "public_exponent", required = true) String publicExponent)
	{
		System.out.println("Client message : " + message);
		System.out.println("Sign Base64 : " + signBase64);
		System.out.println(publicExponent);

		byte[] sign = Base64.decode(signBase64);
		System.out.println("Sign : " + new String(sign));

		RSAPublicKey publicKey = RSA.getPublicKey(new BigInteger(modulus),
				new BigInteger(publicExponent));
		boolean verify = RSA.verify(message, sign, publicKey);

		String m = "From Server";
		KeyPair keyPair = RSA.generateKeyPair();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();

		byte[] encrypted = RSA.encrypt(m, rsaPublicKey);
		byte[] decrypted = RSA.decrypt(encrypted, rsaPrivateKey);

		System.out.println("Original String : " + new String(decrypted));

		BigInteger serverModulus = rsaPublicKey.getModulus();
		BigInteger serverPublicExponent = rsaPublicKey.getPublicExponent();

		byte[] signFromServer = RSA.sign(m, rsaPrivateKey);
		String signFromServerBase64 = Base64.encode(signFromServer);

		System.out.println("Verify : " + verify);

		JSONObject object = new JSONObject();
		object.put("verify", verify);
		object.put("message", m);
		object.put("sign", signFromServerBase64);
		object.put("modulus", serverModulus.toString());
		object.put("public_exponent", serverPublicExponent.toString());

		return object.toString();
	}
}
