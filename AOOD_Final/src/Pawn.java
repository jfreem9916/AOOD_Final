import javax.swing.ImageIcon;

public class Pawn extends Piece {
	protected ImageIcon image;

	public Pawn(int x, int y, char color) {
		super(x, y, color);
		pieceIcon = new ImageIcon(this.getClass().getResource("Pawn" + color + ".png"));
	}

	@Override
	public boolean canReachTile(int x, int y) {
		if(color == 'W'){
			
		}
		else{
			
		}
		return false;
	}




}
