import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tile extends JPanel{
	private Piece piece;
	private JLabel display;
	public Tile(Color bg){
		this.piece = null;
		
		display = new JLabel();
		this.setLayout(null);
		this.setVisible(true);
		this.add(display);
		this.setBackground(bg);
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
}
