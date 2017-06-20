package com.svili.test;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.svili.model.po.User;
import com.svili.model.type.DataState;
import com.svili.model.vo.JsonResponse;

@Controller
public class TestController {

	@RequestMapping(value="/test.do",method=RequestMethod.GET)
	@ResponseBody
	public JsonResponse test(){
		User user = new User();
		user.setDeptId(1);
		user.setCreateTime(new Date());
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
