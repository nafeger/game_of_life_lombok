package com.nate_land.gol_lombok;

import java.nio.file.FileSystems;
import java.nio.file.Files;

public class Driver {
	public static void main(String... args) throws Exception {
		Board b = new Board(Files.readAllLines(FileSystems.getDefault().getPath("examples/example.board")));
		System.out.println(b);
	}
}
