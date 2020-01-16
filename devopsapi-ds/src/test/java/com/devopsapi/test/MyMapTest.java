package com.devopsapi.test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MyMapTest {

	public static void main(String[] args) {
		
		List<Integer> range = IntStream.range(0, 500).boxed().collect(Collectors.toList());
		
		
	}

}
