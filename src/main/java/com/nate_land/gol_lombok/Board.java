package com.nate_land.gol_lombok;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import lombok.Value;

/**
 *
 */
@Value public class Board {
	private final int size;
	private final int maxIterations;
	private final int currentIteration;
	private final List<String> gameBoard;
	
	public Board(final Board initialBoard) {
		this.size = initialBoard.size;
		this.gameBoard = this.buildBoard(initialBoard.gameBoard);
		this.currentIteration = initialBoard.currentIteration+1;
		this.maxIterations = initialBoard.maxIterations;
		initialBoard.nodes(initialBoard).forEach(n -> {
			final long nCount = n.neighborCount();
			if (!n.isAlive()) {
				if (nCount == 3) {
					setNode(n, true);
				}
			} else {
				List<String> neigh = n.neighbors().map(ne -> ne.isAlive()?"x":"").collect(Collectors.<String>toList());
				List<Node> nodes = n.neighbors().collect(Collectors.<Node>toList());
				if (nCount < 2) {
					setNode(n, false);
				} else if (nCount == 2 || nCount == 3) {
					setNode(n, true);
				} else if (nCount > 3) {
					setNode(n, false);
				}
			}
		});
	}

	private void setNode(Node n, boolean alive) {
		StringBuilder sb = new StringBuilder(this.gameBoard.get(n.getRow()));
		sb.setCharAt(n.getColumn(), alive ? 'x' : UNSET);
		this.gameBoard.set(n.getRow(), sb.toString());
	}

	Stream<Node> nodes(Board initialBoard) {
		return IntStream.range(0, size*size).mapToObj(i -> Node.of(initialBoard, i / this.size, i % this.size));
	}

	public Board(List<String> readBoard, int maxIterations, int currentIteration) {
		this.size = Math.max(readBoard.size(), readBoard.parallelStream().map(l -> l.length()).max(Integer::compare).get());
		this.maxIterations = maxIterations;
		this.currentIteration = currentIteration;

		this.gameBoard = this.buildBoard(readBoard);
	}
	private static final Character UNSET = ' ';
	private List<String> buildBoard(List<String> readBoard) {
//		ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();
		List<String> builder = Lists.newLinkedList();
		for(int i = 0; i < size; i++) {
			if (i >= readBoard.size()) {
				String chars = IntStream.range(0,size).mapToObj(x -> " ").collect(Collectors.joining(""));
				builder.add(chars);
				continue;
			}
			StringBuilder chars = new StringBuilder(size);
			final String row = readBoard.get(i);
			for(int j = 0; j < size; j++) {
				if (j < row.length() ) {
					char c = row.charAt(j);
					if (c == 'x') {
						int x = 1;
					}
					chars.append(c);
				} else {
					chars.append(UNSET);
				}
			}
		
			builder.add(chars.toString());
		}
		return builder;
	}

	@Override
	public String toString() {
//		StringBuilder sb = new StringBuilder();
		return this.gameBoard.stream().collect(Collectors.joining("\n"));
				
//				.forEach(l -> {
//			sb.append(l);
//			sb.append("\n");
//		} );
//		return sb.toString();
	}

	public boolean hasNext() {
		return this.maxIterations > this.currentIteration;
	}

	@Value(staticConstructor = "of")
	public static class Node {
		private final Board board;
		private final int row;
		private final int column;
		
		public String toString() {
			return String.format("[%s,%s]=%s",row, column, this.isAlive());
		}

		public boolean isAlive() {
			return board.gameBoard.get(row).charAt(column) == 'x';
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
			if (row - 1 >= 0 && column + 1 < board.size) { //top right
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
