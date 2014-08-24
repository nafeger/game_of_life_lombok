package com.nate_land.gol_lombok;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

public class Driver {
	public static void main(String... args) throws Exception {
		if (args.length != 3) {
			System.out.println(" [filename] [iterations] [delay in ms]");
			System.exit(-1);
		}
		List<String> file = Files.readAllLines(FileSystems.getDefault().getPath(args[0]));
		int iterations = Integer.parseInt(args[1]);
		
		Board b = new Board(file, iterations , 0);
		while(b.hasNext()) {
			System.out.println("iteration: "+b.getCurrentIteration());
			System.out.println(b);
			b = new Board(b);
			Thread.sleep(Integer.parseInt(args[2]));
			Runtime.getRuntime().exec("clear");
		}
	}
}
