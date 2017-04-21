import javax.swing.ImageIcon;

public class King extends Piece {
	protected ImageIcon image;
	public King(int x, int y, char color) {
		super(x, y, color);
		pieceIcon = new ImageIcon(this.getClass().getResource("King" + color + ".png"));
		value = Integer.MAX_VALUE;
	}

	@Override
	public boolean canReachTile(int xCoord, int yCoord, boolean tF, char c) {
		if(c == this.color){
			return false;
		}
		if(Math.abs(xCoord - this.x) <= 1 && Math.abs(yCoord - this.y) <= 1){
			return true;
		}
		return false;
		
	}




}
