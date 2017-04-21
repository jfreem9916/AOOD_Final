import javax.swing.ImageIcon;

public class Queen extends Piece {
	protected ImageIcon image;
	public Queen(int x, int y, char color) {
		super(x, y, color);
		pieceIcon = new ImageIcon(this.getClass().getResource("Queen" + color + ".png"));
		value = 9;
	}

	@Override
	public boolean canReachTile(int xCoord, int yCoord, boolean tF, char c) {
		if(c == this.color){
			return false;
		}
		int xChange =  Math.abs(xCoord - this.x);
		int yChange =  Math.abs(yCoord - this.y);
		if(xChange == 0 || yChange == 0){
			return true;
		}
		if(xChange == yChange){
			return true;
		}
		return false;
		
	}




}
