package com.Lee.sso.service;


import com.Lee.dto.E3Result;
import com.Lee.pojo.TbUser;

public interface RegisterService {

	E3Result checkData(String param, int type);
	E3Result register(TbUser user);
}
