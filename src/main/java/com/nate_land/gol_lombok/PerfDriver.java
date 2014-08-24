package com.nate_land.gol_lombok;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Attempt to calculate performance of system.
 */
public class PerfDriver {

	public static void main(String... args) throws IOException, InterruptedException {
		if (args.length != 2) {
			System.out.println(" [filename] [iterations] ");
			System.exit(-1);
		}
		Board b = new Board(Files.readAllLines(FileSystems.getDefault().getPath(args[0])), Integer.parseInt(args[1]), 0);
		long time = System.currentTimeMillis();
		while (b.hasNext()) {
			b = new Board(b);
		}
		System.out.println("Total time: "+(System.currentTimeMillis() - time));
	}
}
