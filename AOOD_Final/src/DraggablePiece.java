import javax.swing.JLabel;
import javax.swing.JPanel;

public class DraggablePiece extends JPanel {
	private Piece myPiece;
	private JLabel display;
	public DraggablePiece(Piece p){
		this.myPiece = p;
		this.setOpaque(false);
		this.setLayout(null);
		display = new JLabel();
		display.setIcon(myPiece.getPieceIcon());
		this.add(display);
		display.setBounds(0, 0, 85, 85);
	}
	public Piece getMyPiece() {
		return myPiece;
	}
	public void setMyPiece(Piece myPiece) {
		this.myPiece = myPiece;
	}
	
	@Override
	public boolean equals(Object o){
		return myPiece.equals(((DraggablePiece) o).getMyPiece());
		
	}
}