package edu.esi.uclm.es.ds.games.domain;


public class Board {
	private byte[][] board;
	
	public Board() {
		this.board = new byte[7][6];
	}
	
	public byte[][]getBoard(){
		return board;
	}

}
