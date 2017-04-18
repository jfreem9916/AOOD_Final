import javax.swing.ImageIcon;

public class Pawn extends Piece {
	protected ImageIcon image;
	protected boolean atInitialPos;
	public Pawn(int x, int y, char color) {
		super(x, y, color);
		pieceIcon = new ImageIcon(this.getClass().getResource("Pawn" + color + ".png"));
		atInitialPos = true;
	}

	@Override
	public boolean canReachTile(int xCoord, int yCoord, boolean tF, char c) {
		if(tF){
			if(this.color == 'B'){
				if(yCoord == this.y + 1 && c != this.color && Math.abs(xCoord - this.x) == 1){
					return true;
				}
			}
			else{
				if(yCoord == this.y - 1 && c != this.color && Math.abs(xCoord - this.x) == 1){
					return true;
				}
			}
			
		}
		else{
			if(this.color == 'B'){
				if(atInitialPos && yCoord == this.y+2 && xCoord == this.x){
					return true;
				}
				if(yCoord == this.y+1 && xCoord == this.x){
					return true;
				}
			}
			else{
				if(atInitialPos && yCoord == this.y-2 && xCoord == this.x){
					return true;
				}
				if(yCoord == this.y-1 && xCoord == this.x){
					return true;
				}
			}

		}
		return false;
		
	}

	public boolean isAtInitalPos(){
		return atInitialPos;
	}

	public void moved() {
		atInitialPos = false;
		
	}


}
