package com.nate_land.gol_lombok;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import lombok.Value;

/**
 *
 */
public class Board {
	private final int size;
	private final int maxIterations;
	private final int currentIteration;
	private final List<List<Character>> gameBoard;
	
	public Board(Board board) {
		this.gameBoard = board.gameBoard;
		this.size = board.size;
		this.currentIteration = board.currentIteration+1;
		this.maxIterations = board.maxIterations;
		nodes().forEach(n -> {
			if (!n.isAlive()) {
				if (n.neighborCount() == 3) {
					setNode(n, true);
				}
			} else {
				if (n.neighborCount() < 2) {
					setNode(n, false);
				} else if (n.neighborCount() == 2 || n.neighborCount() == 3) {
					setNode(n, true);
				} else if (n.neighborCount() > 3) {
					setNode(n, false);
				}
			}
		});
	}

	private void setNode(Node n, boolean alive) {
		this.gameBoard.get(n.getRow()).set(n.getColumn(), alive ? 'x' : ' ');
	}

	Stream<Node> nodes() {
		return IntStream.range(0, size*size-1).mapToObj(i -> Node.of(this, i / this.size, i % this.size));
	}

	public Board(List<String> readBoard, int maxIterations, int currentIteration) {
		this.size = Math.max(readBoard.size(), readBoard.parallelStream().map(l -> l.length()).max(Integer::compare).get());
		this.maxIterations = maxIterations;
		this.currentIteration = currentIteration;

		ImmutableList.Builder<List<Character>> builder = buildBoard(readBoard);
		this.gameBoard = builder.build();
	}

	private ImmutableList.Builder<List<Character>> buildBoard(List<String> readBoard) {
		ImmutableList.Builder<List<Character>> builder = new ImmutableList.Builder<>();
		for(int i = 0; i < size; i++) {
			List<Character> chars = Lists.newArrayListWithExpectedSize(size);
			for(int j = 0; j < size; j++) {
				chars.add(readBoard.get(i).charAt(j));
			}
		
			builder.add(chars);
		}
		List<List<Character>> board = builder.build();
		return builder;
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

	public boolean hasNext() {
		return this.maxIterations > this.currentIteration;
	}

	@Value(staticConstructor = "of")
	public static class Node {
		private final Board board;
		private final int row;
		private final int column;

		public boolean isAlive() {
			return board.gameBoard.get(row).get(column) == 'x';
		}

		public long neighborCount() {
			return neighbors().filter(n -> n.isAlive()).count();
		}

		/**
		 * position
		 * 012 
		 * 3x4 
		 * 567
		 * @return
		 */
		public Stream<Node> neighbors() {
			List<Node> rv = Lists.newLinkedList();
			if (row - 1 >= 0 && column - 1 >= 0) { // top left
				rv.add(of(this.board, row - 1, column - 1));
			}
			if (row - 1 >= 0) { // top
				rv.add(of(this.board, row - 1, column ));
			}
			if (row - 1 >- 0 && column + 1 < board.size) { //top right
				rv.add(of(this.board, row - 1, column + 1));
			}
			if (column - 1 >= 0) { // left
				rv.add(of(this.board, row , column - 1));
			}
			if (column + 1 < board.size) { // right
				rv.add(of(this.board, row , column + 1));
			}
			if (row + 1 < board.size && column - 1 >= 0) { // bottom left
				rv.add(of(this.board, row + 1, column - 1));
			}
			if (row + 1 < board.size ) { // bottom 
				rv.add(of(this.board, row + 1, column ));
			}
			if (row + 1 < board.size && column + 1 < board.size) { // bottom right
				rv.add(of(this.board, row + 1, column + 1));
			}
			
			return rv.stream();
		}
	}
}
