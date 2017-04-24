import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tile extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8301016372483008823L;
	private Piece piece;
	private Color bg;

	public Tile(Color bg) {
		this.piece = null;
		this.bg = bg;
		this.setVisible(true);
		this.setBackground(bg);

	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;

	}

	public boolean isEmpty() {
		return piece == null;
	}
	

	public boolean equalsTile(Tile t){
		if(t.getX() == this.getX() && t.getY() == this.getY()){
			return true;
		}
		return false;
	}
}
