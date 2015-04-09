package com.moviedecision;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import smile.Network;

public class MovieByHelpertest {

	@Test
	public void test() {
		Network net = new Network();
		String xdslFileIn = (new File("models/changedID.xdsl")).toString();
        net.readFile(xdslFileIn);
		
		
		
		
		assertEquals("1", "1");
		fail("Not yet implemented");
	}

}
