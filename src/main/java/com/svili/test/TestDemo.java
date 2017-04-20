package com.svili.test;

import java.util.ArrayList;
import java.util.List;

public class TestDemo {
	public static void main(String[] args) throws Exception {
		//upload();
		Object obj = null;
		String str = obj + "";
		System.out.println(str);
		List<String> list = new ArrayList<String>();
		if(list.equals(str)){
			System.out.println(1);
		}else{
			System.out.println(0);
		}
		
	}
}
