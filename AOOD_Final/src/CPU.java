import java.util.ArrayList;

public class CPU {
	private Tile[][] board;
	private ArrayList<Piece> myPieces;

	public CPU(Tile[][] b, ArrayList<DraggablePiece> p) {
		board = b;
		myPieces = new ArrayList<Piece>();
		for (DraggablePiece dp : p) {
			myPieces.add(dp.getMyPiece());
		}
	}

	public Tile[] decideMove(){
		Tile[] output = new Tile[2];
		
		
		for(Piece p: myPieces){
			int pX = p.getX();
			int pY = p.getY();
			if(p instanceof Pawn){
				
				output[0] = board[pX][pY];
				Pawn pawn = (Pawn) p;
				if(coordExists(pX+1, pY+1) && coordHasPiece(pX+1, pY+1) && pieceIsEnemy(pX+1, pY+1)) {
					output[1] = board[pX+1][pY+1];
					return output;
				}
				if(coordExists(pX-1, pY+1) && coordHasPiece(pX-1, pY+1) && pieceIsEnemy(pX-1, pY+1)) {
					output[1] = board[pX-1][pY+1];
					return output;
				}
				if(pawn.isAtInitalPos() && coordExists(pX, pY+2) && !coordHasPiece(pX, pY+2)){
					output[1] = board[pX][pY+2];
					return output;
				}
				if(coordExists(pX, pY+1) && !coordHasPiece(pX, pY+1)){
					output[1] = board[pX][pY+1];
					return output;
				}
				
				
			}
		}
		
		
		
		return null;
	}

	public boolean coordExists(int x, int y) {
		if (x < 0 || x > 7 || y > 7 || y < 0) {
			return false;
		}
		return true;
	}

	public boolean coordHasPiece(int x, int y) {
		if (!coordExists(x, y)) {
			return false;
		}
		if (board[x][y].getPiece() == null) {
			return false;
		}
		return true;
	}

	public boolean pieceIsEnemy(int x, int y) {
		if (!coordHasPiece(x, y)) {
			return false;
		}
		if (board[x][y].getPiece().getColor() == 'B') {
			return false;
		}
		return true;
	}
	
	public int checkNetGain(Piece p1, Piece p2){
		return 0;
	}
}
