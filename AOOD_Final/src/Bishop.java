import javax.swing.ImageIcon;

public class Bishop extends Piece {
	protected ImageIcon image;
	public Bishop(int x, int y, char color) {
		super(x, y, color);
		pieceIcon = new ImageIcon(this.getClass().getResource("Bishop" + color + ".png"));
		value = 3;
	}

	@Override
	public boolean canReachTile(int xCoord, int yCoord, boolean tF, char c) {
		if(c == this.color){
			return false;
		}
		if(Math.abs(xCoord - this.x) == Math.abs(yCoord - this.y)){
			return true;
		}
		return false;
		
	}




}
