import java.awt.Color;

import javax.swing.JFrame;

public class ChessBoard extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5656815955239111516L;
	private Tile[][] board;
	private int width, height;
	public ChessBoard(){
		super("Chess");
		
		board = new Tile[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(j % 2 == i % 2){
					board[i][j] = new Tile(Color.black);
				}
				else{
					board[i][j] = new Tile(Color.white);
				}
			}
		}
		
		width = 800;
		height = 800;
		
		this.setSize(width, height);
		this.setLocation((1600-width)/2, (900-height)/2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}

}
