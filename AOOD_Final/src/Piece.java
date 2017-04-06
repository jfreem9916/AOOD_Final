import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class Piece extends JPanel {
	protected int x, y;
	protected char color;
	protected ImageIcon pieceIcon;
	protected JLabel display;
	public Piece(int x, int y, char color){
		this.x = x;
		this.y = y;
		this.color = color;
		display = new JLabel();
		
		this.setLayout(null);
		this.setVisible(true);
		
		this.add(display);
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
	public char getColor() {
		return color;
	}
	public void setColor(char color) {
		this.color = color;
	}
}
