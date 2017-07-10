package com.svili.junit;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

public class SsmTest {

	public static void main(String[] args) {
		int[] array = { 1, 2 };
		Exception ex = new Exception();
		try {
			System.out.println(array[3]);
		} catch (Exception e) {
			ex = new Exception("hehhehhe");
			
			e.printStackTrace();
		}
		System.out.println(ex.getMessage());
	}

}
