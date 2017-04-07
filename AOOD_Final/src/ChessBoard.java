import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

public class ChessBoard extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5656815955239111516L;
	private Tile[][] board;
	private int width, height;
	private ArrayList<Piece> playerPieces;
	private ArrayList<Piece> cpuPieces;
	public ChessBoard(){ 
		super("Chess");
		
		width = 696;
		height = 718;
		int tileSize = 85;
		
		this.setSize(width, height);
		this.setLocation((1600-width)/2, (900-height)/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLayout(null);
		
		board = new Tile[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(j % 2 == i % 2){
					Tile newTile = new Tile(new Color(242, 240 ,215)); 
					board[i][j] = newTile;
					this.add(newTile);
					newTile.setBounds(j * tileSize, i * tileSize, tileSize, tileSize);
					
				}
				else{
					Tile newTile = new Tile(new Color(147, 88 ,28)); 
					board[i][j] = newTile;
					this.add(newTile);
					newTile.setBounds(j * tileSize, i * tileSize, tileSize, tileSize);
				}
			}
		}
		
		playerPieces = new ArrayList<Piece>();
		cpuPieces = new ArrayList<Piece>();

		this.addNewPiece('P', 'W', 7, 0);
		
		
	
	}
	
	
	
	
	public void addNewPiece(char type, char color, int x, int y){
		Piece p = null;
		if(type == 'P'){
			p = new Pawn(x, y, color);
		}
		
		
		if(board[x][y].getPiece() != null){
			return;
		}
		
		if(color == 'W'){
			playerPieces.add(p);
			board[x][y].setPiece(p);
		}
		else{
			cpuPieces.add(p);
			board[x][y].setPiece(p);
		}
	}

}
