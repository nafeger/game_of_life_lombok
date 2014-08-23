package com.nate_land.gol_lombok;

import java.util.List;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 *
 */
public class Board {
	private final int size;
	private final List<List<Character>> gameBoard;
	
	public Board(int size) {
		this.size = size;
		ImmutableList.Builder<List<Character>> builder = new ImmutableList.Builder<>();
		for(int i = 0; i < size; i++) {
			List<Character> chars = Lists.newArrayListWithExpectedSize(size);
			IntStream.range(0,size).forEach(t -> chars.add(' '));
			builder.add(chars);
		}
		this.gameBoard = builder.build();
	}

	public Board(List<String> readBoard) {
		this.size = Math.max(readBoard.size(), readBoard.parallelStream().map(l -> l.length()).max(Integer::compare).get());
		
		ImmutableList.Builder<List<Character>> builder = new ImmutableList.Builder<>();
		for(int i = 0; i < size; i++) {
			List<Character> chars = Lists.newArrayListWithExpectedSize(size);
			for(int j = 0; j < size; j++) {
				chars.add(readBoard.get(i).charAt(j));
			}
		
			builder.add(chars);
		}
		this.gameBoard = builder.build();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.gameBoard.stream().forEach(l -> {
			l.stream().forEach(c -> sb.append(c));
			sb.append("\n");
		} );
		return sb.toString();
	}

}
