package com.nate_land.gol_lombok;

import java.nio.file.FileSystems;
import java.nio.file.Files;

public class Driver {
	public static void main(String... args) throws Exception {
		Board b = new Board(Files.readAllLines(FileSystems.getDefault().getPath("examples/example.board")), 10, 0);
		while(b.hasNext()) {
			System.out.println("top");
			System.out.println(b);
			b = new Board(b);
			Thread.sleep(15 * 100);
			Runtime.getRuntime().exec("clear");
//			System.out.print("\u001b[2J");
//			System.out.print("\b");
//			System.out.flush();
		}
	}
}
