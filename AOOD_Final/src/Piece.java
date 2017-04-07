import javax.swing.ImageIcon;


public abstract class Piece {
	protected int x, y;
	protected char color;
	protected ImageIcon pieceIcon;
	public Piece(int x, int y, char color){
		this.x = x;
		this.y = y;
		this.color = color;

	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public ImageIcon getPieceIcon() {
		return pieceIcon;
	}
	public void setPieceIcon(ImageIcon pieceIcon) {
		this.pieceIcon = pieceIcon;
	}
	public char getColor() {
		return color;
	}
	public void setColor(char color) {
		this.color = color;
	}
	@Override
	public boolean equals(Object o){
		Piece p = (Piece)o;
		if(p.getX() == this.getX() && p.getY() == this.getY() && this.getColor() == p.getColor()){
			return true;
		}
		return false;
		
	}
	public abstract boolean canReachTile(int x, int y);
}
