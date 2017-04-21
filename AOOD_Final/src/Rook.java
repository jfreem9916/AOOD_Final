import javax.swing.ImageIcon;

public class Rook extends Piece {
	protected ImageIcon image;
	public Rook(int x, int y, char color) {
		super(x, y, color);
		pieceIcon = new ImageIcon(this.getClass().getResource("Rook" + color + ".png"));
		value = 5;
	}

	@Override
	public boolean canReachTile(int xCoord, int yCoord, boolean tF, char c) {
		if(c == this.color){
			return false;
		}
		if(xCoord - this.x == 0 || yCoord - this.y == 0){
			return true;
		}
		return false;
		
	}




}
