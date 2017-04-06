import java.util.ArrayList;

public class Player {
	protected String name;
	protected ArrayList<Piece> allPieces;
	public Player(String name){
		this.name = name;
		allPieces = new ArrayList<Piece>();
	}
	
	public void addPiece(Piece p){
		allPieces.add(p);
	}
}
