package com.svili.test;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.svili.model.JsonResponse;
import com.svili.portal.pojo.User;
import com.svili.portal.type.DataState;

@Controller
public class TestController {

	@RequestMapping(value="/test.do",method=RequestMethod.GET)
	@ResponseBody
	public JsonResponse test(){
		User user = new User();
		user.setDeptId(1);
		user.setCreateTime(new Date());
		user.setState(DataState.EFFECT);
		return new JsonResponse(user);
	}
	
	@RequestMapping(value="/test2.page",method=RequestMethod.GET)
	public String test2(){
//		System.out.println(111);
//		int[] array = {1,2};
		//System.out.println(array[3]);
		return "/";
	}
}
