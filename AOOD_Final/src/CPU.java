import java.util.ArrayList;

public class CPU {
	private Tile[][] board;
	private ArrayList<Piece> myPieces;
	public CPU(Tile[][] b, ArrayList<DraggablePiece> p){
		board = b;
		myPieces = new ArrayList<Piece>();
		for(DraggablePiece dp: p){
			myPieces.add(dp.getMyPiece());
		}
	}
	public Tile[] decideMove(){
		Tile[] output = new Tile[2];
		return output;
	}
}
