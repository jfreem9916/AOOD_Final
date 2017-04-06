import javax.swing.JFrame;

public class ChessBoard extends JFrame{
	private Piece[][] board;
	private int width, height;
	public ChessBoard(){
		board = new Piece[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				board[i][j] = null;
			}
		}
		
		this.setSize(width, height);
		this.setLocation(1600-width/2, 900-height/2);
		this.setVisible(true);
		
	}

}
