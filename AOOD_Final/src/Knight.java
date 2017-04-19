import javax.swing.ImageIcon;

public class Knight extends Piece {
	protected ImageIcon image;
	public Knight(int x, int y, char color) {
		super(x, y, color);
		pieceIcon = new ImageIcon(this.getClass().getResource("Knight" + color + ".png"));
	}

	@Override
	public boolean canReachTile(int xCoord, int yCoord, boolean tF, char c) {
		if(c == this.color){
			return false;
		}
		int xChange =  Math.abs(xCoord - this.x);
		int yChange =  Math.abs(yCoord - this.y);
		if((xChange == 1 && yChange == 2) || (xChange == 2 && yChange == 1)){
			return true;
		}
		return false;
		
	}




}
