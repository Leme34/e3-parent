package com.Lee.sso.service;


import com.Lee.dto.E3Result;

/**
 * 根据token查询用户信息
 */
public interface TokenService {

	E3Result getUserByToken(String token);
}
