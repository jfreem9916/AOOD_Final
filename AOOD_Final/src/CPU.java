import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

public class CPU {
	private Tile[][] board;
	private ArrayList<Piece> myPieces;
	private ArrayList<DraggablePiece> piecesOnBoard;

	public CPU(Tile[][] b, ArrayList<DraggablePiece> p) {
		board = b;
		piecesOnBoard = p;
		myPieces = new ArrayList<Piece>();
		for (DraggablePiece dp : piecesOnBoard) {
			myPieces.add(dp.getMyPiece());
		}
	}

	public Tile[] decideMove() {
		this.updateArrayList();
		Tile[] output = null;
		if (this.kingCanBeJumped()) {
			JOptionPane.showMessageDialog(null, "The cpu's king is in check!", "Check",
					JOptionPane.INFORMATION_MESSAGE);
			output = moveKingToSafety();
		}
		if (output == null) {
			output = checkForJump();
			if (output == null) {
				Collections.shuffle(myPieces);
				output = checkForMove();
				if (output == null) {
					output = lastResort();
				}
			}
		}
		return output;
	}

	public Tile[] checkForJump() {
		Tile[] output = new Tile[2];
		for (Piece p : myPieces) {
			int pX = p.getX();
			int pY = p.getY();
			if (p instanceof Pawn) {
				output[0] = board[pX][pY];
				if (coordExists(pX + 1, pY + 1) && coordHasPiece(pX + 1, pY + 1) && pieceIsEnemy(pX + 1, pY + 1)
						&& this.checkNetGain(p, pX + 1, pY + 1) >= 0) {
					output[1] = board[pX + 1][pY + 1];
					return output;
				}
				if (coordExists(pX - 1, pY + 1) && coordHasPiece(pX - 1, pY + 1) && pieceIsEnemy(pX - 1, pY + 1)
						&& this.checkNetGain(p, pX + 1, pY + 1) >= 0) {
					output[1] = board[pX - 1][pY + 1];
					return output;
				}
			} else if (p instanceof Rook) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();
				int tempX = pX;
				for (int i = pX + 1; i < 8 && board[i][pY].getPiece() == null; i++) {
					tempX = i;
				}
				if (this.coordExists(tempX + 1, pY) && this.coordHasPiece(tempX + 1, pY)
						&& this.pieceIsEnemy(tempX + 1, pY)) {
					posCoords.add(new intPair(tempX + 1, pY));
				}

				tempX = pX;
				for (int i = pX - 1; i >= 0 && board[i][pY].getPiece() == null; i--) {
					tempX = i;
				}
				if (this.coordExists(tempX - 1, pY) && this.coordHasPiece(tempX - 1, pY)
						&& this.pieceIsEnemy(tempX - 1, pY)) {
					posCoords.add(new intPair(tempX - 1, pY));
				}

				int tempY = pY;
				for (int i = pY + 1; i < 8 && board[pX][i].getPiece() == null; i++) {
					tempY = i;
				}
				if (this.coordExists(pX, tempY + 1) && this.coordHasPiece(pX, tempY + 1)
						&& this.pieceIsEnemy(pX, tempY + 1)) {
					posCoords.add(new intPair(pX, tempY + 1));
				}

				tempY = pY;
				for (int i = pY - 1; i >= 0 && board[pX][i].getPiece() == null; i--) {
					tempY = i;
				}
				if (this.coordExists(pX, tempY - 1) && this.coordHasPiece(pX, tempY - 1)
						&& this.pieceIsEnemy(pX, tempY - 1)) {
					posCoords.add(new intPair(pX, tempY - 1));
				}

				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)
							&& this.checkNetGain(p, tempX, tempY) >= 0) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Knight) {
				output[0] = board[pX][pY];
				intPair[] posCoords = new intPair[8];
				posCoords[0] = new intPair(pX + 1, pY - 2);
				posCoords[1] = new intPair(pX + 2, pY - 1);
				posCoords[2] = new intPair(pX + 2, pY + 1);
				posCoords[3] = new intPair(pX + 1, pY + 2);
				posCoords[4] = new intPair(pX - 1, pY + 2);
				posCoords[5] = new intPair(pX - 2, pY + 1);
				posCoords[6] = new intPair(pX - 2, pY - 1);
				posCoords[7] = new intPair(pX - 1, pY - 2);
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)
							&& this.checkNetGain(p, tempX, tempY) >= 0) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Bishop) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();

				int tempChange = 0;
				for (int i = 1; pX + i < 8 && pY + i < 8 && board[pX + i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX + tempChange + 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY + tempChange + 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY - i >= 0 && board[pX - i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX - tempChange - 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX + i < 8 && pY - i >= 0 && board[pX + i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX + tempChange + 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY + i < 8 && board[pX - i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX - tempChange - 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY + tempChange + 1));
				}

				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)
							&& this.checkNetGain(p, tempX, tempY) >= 0) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Queen) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();

				// Rook half
				int tempX = pX;
				for (int i = pX + 1; i < 8 && board[i][pY].getPiece() == null; i++) {
					tempX = i;
				}
				if (this.coordExists(tempX + 1, pY) && this.coordHasPiece(tempX + 1, pY)
						&& this.pieceIsEnemy(tempX + 1, pY)) {
					posCoords.add(new intPair(tempX + 1, pY));
				}

				tempX = pX;
				for (int i = pX - 1; i >= 0 && board[i][pY].getPiece() == null; i--) {
					tempX = i;
				}
				if (this.coordExists(tempX - 1, pY) && this.coordHasPiece(tempX - 1, pY)
						&& this.pieceIsEnemy(tempX - 1, pY)) {
					posCoords.add(new intPair(tempX - 1, pY));
				}

				int tempY = pY;
				for (int i = pY + 1; i < 8 && board[pX][i].getPiece() == null; i++) {
					tempY = i;
				}
				if (this.coordExists(pX, tempY + 1) && this.coordHasPiece(pX, tempY + 1)
						&& this.pieceIsEnemy(pX, tempY + 1)) {
					posCoords.add(new intPair(pX, tempY + 1));
				}

				tempY = pY;
				for (int i = pY - 1; i >= 0 && board[pX][i].getPiece() == null; i--) {
					tempY = i;
				}
				if (this.coordExists(pX, tempY - 1) && this.coordHasPiece(pX, tempY - 1)
						&& this.pieceIsEnemy(pX, tempY - 1)) {
					posCoords.add(new intPair(pX, tempY - 1));
				}

				// Bishop half
				int tempChange = 0;
				for (int i = 1; pX + i < 8 && pY + i < 8 && board[pX + i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX + tempChange + 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY + tempChange + 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY - i >= 0 && board[pX - i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX - tempChange - 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX + i < 8 && pY - i >= 0 && board[pX + i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX + tempChange + 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY + i < 8 && board[pX - i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (this.coordExists(pX - tempChange - 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY + tempChange + 1));
				}

				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)
							&& this.checkNetGain(p, tempX, tempY) >= 0) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
			} else {
				output[0] = board[pX][pY];
				intPair[] posCoords = new intPair[8];
				posCoords[0] = new intPair(pX, pY - 1);
				posCoords[1] = new intPair(pX + 1, pY - 1);
				posCoords[2] = new intPair(pX + 1, pY);
				posCoords[3] = new intPair(pX + 1, pY + 1);
				posCoords[4] = new intPair(pX, pY + 1);
				posCoords[5] = new intPair(pX - 1, pY + 1);
				posCoords[6] = new intPair(pX - 1, pY);
				posCoords[7] = new intPair(pX - 1, pY - 1);
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)
							&& this.checkNetGain(p, tempX, tempY) >= 0) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
			}

		}

		return null;
	}

	public Tile[] checkForMove() {
		Tile[] output = new Tile[2];
		for (Piece p : myPieces) {
			int pX = p.getX();
			int pY = p.getY();
			if (p instanceof Pawn) {
				output[0] = board[pX][pY];
				Pawn pawn = (Pawn) p;
				if (pawn.isAtInitalPos() && coordExists(pX, pY + 2) && !coordHasPiece(pX, pY + 2)
						&& !canBeJumped(pX, pY + 2) && !coordHasPiece(pX, pY+1)) {
					output[1] = board[pX][pY + 2];
					return output;
				}
				if (coordExists(pX, pY + 1) && !coordHasPiece(pX, pY + 1) && !canBeJumped(pX, pY + 1)) {
					output[1] = board[pX][pY + 1];
					return output;
				}
			} else if (p instanceof Rook) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();
				int tempX = pX;
				for (int i = pX + 1; i < 8 && board[i][pY].getPiece() == null; i++) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}

				tempX = pX;
				for (int i = pX - 1; i >= 0 && board[i][pY].getPiece() == null; i--) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}

				int tempY = pY;
				for (int i = pY + 1; i < 8 && board[pX][i].getPiece() == null; i++) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}

				tempY = pY;
				for (int i = pY - 1; i >= 0 && board[pX][i].getPiece() == null; i--) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}

				Collections.shuffle(posCoords);

				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY) && !canBeJumped(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Knight) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();
				posCoords.add(new intPair(pX + 1, pY - 2));
				posCoords.add(new intPair(pX + 2, pY - 1));
				posCoords.add(new intPair(pX + 2, pY + 1));
				posCoords.add(new intPair(pX + 1, pY + 2));
				posCoords.add(new intPair(pX - 1, pY + 2));
				posCoords.add(new intPair(pX - 2, pY + 1));
				posCoords.add(new intPair(pX - 2, pY - 1));
				posCoords.add(new intPair(pX - 1, pY - 2));
				Collections.shuffle(posCoords);

				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY) && !canBeJumped(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Bishop) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();

				int tempChange = 0;
				for (int i = 1; pX + i < 8 && pY + i < 8 && board[pX + i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY + tempChange));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY - i >= 0 && board[pX - i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY - tempChange));
				}

				tempChange = 0;
				for (int i = 1; pX + i < 8 && pY - i >= 0 && board[pX + i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY - tempChange));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY + i < 8 && board[pX - i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY + tempChange));
				}

				Collections.shuffle(posCoords);

				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY) && !canBeJumped(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Queen) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();

				// Rook half
				int tempX = pX;
				for (int i = pX + 1; i < 8 && board[i][pY].getPiece() == null; i++) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}

				tempX = pX;
				for (int i = pX - 1; i >= 0 && board[i][pY].getPiece() == null; i--) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}

				int tempY = pY;
				for (int i = pY + 1; i < 8 && board[pX][i].getPiece() == null; i++) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}

				tempY = pY;
				for (int i = pY - 1; i >= 0 && board[pX][i].getPiece() == null; i--) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}

				// Bishop half
				int tempChange = 0;
				for (int i = 1; pX + i < 8 && pY + i < 8 && board[pX + i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY + tempChange));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY - i >= 0 && board[pX - i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY - tempChange));
				}

				tempChange = 0;
				for (int i = 1; pX + i < 8 && pY - i >= 0 && board[pX + i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY - tempChange));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY + i < 8 && board[pX - i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY + tempChange));
				}

				Collections.shuffle(posCoords);

				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY) && !canBeJumped(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
			} /*else {
				
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();
				posCoords.add(new intPair(pX, pY - 1));
				posCoords.add(new intPair(pX + 1, pY - 1));
				posCoords.add(new intPair(pX + 1, pY));
				posCoords.add(new intPair(pX + 1, pY + 1));
				posCoords.add(new intPair(pX, pY + 1));
				posCoords.add(new intPair(pX - 1, pY + 1));
				posCoords.add(new intPair(pX - 1, pY));
				posCoords.add(new intPair(pX - 1, pY - 1));

				Collections.shuffle(posCoords);
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY) && !canBeJumped(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
			}
			*/

		}

		return null;
	}

	public Tile[] lastResort() {
		Tile[] output = new Tile[2];
		for (Piece p : myPieces) {
			int pX = p.getX();
			int pY = p.getY();
			if (p instanceof Pawn) {
				output[0] = board[pX][pY];
				Pawn pawn = (Pawn) p;
				if (coordExists(pX + 1, pY + 1) && coordHasPiece(pX + 1, pY + 1) && pieceIsEnemy(pX + 1, pY + 1)) {
					output[1] = board[pX + 1][pY + 1];
					return output;
				}
				if (coordExists(pX - 1, pY + 1) && coordHasPiece(pX - 1, pY + 1) && pieceIsEnemy(pX - 1, pY + 1)) {
					output[1] = board[pX - 1][pY + 1];
					return output;
				}
				if (pawn.isAtInitalPos() && coordExists(pX, pY + 2) && !coordHasPiece(pX, pY + 2)) {
					output[1] = board[pX][pY + 2];
					return output;
				}
				if (coordExists(pX, pY + 1) && !coordHasPiece(pX, pY + 1)) {
					output[1] = board[pX][pY + 1];
					return output;
				}
			} else if (p instanceof Rook) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();
				int tempX = pX;
				for (int i = pX + 1; i < 8 && board[i][pY].getPiece() == null; i++) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}
				if (this.coordExists(tempX + 1, pY) && this.coordHasPiece(tempX + 1, pY)
						&& this.pieceIsEnemy(tempX + 1, pY)) {
					posCoords.add(new intPair(tempX + 1, pY));
				}

				tempX = pX;
				for (int i = pX - 1; i >= 0 && board[i][pY].getPiece() == null; i--) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}
				if (this.coordExists(tempX - 1, pY) && this.coordHasPiece(tempX - 1, pY)
						&& this.pieceIsEnemy(tempX - 1, pY)) {
					posCoords.add(new intPair(tempX - 1, pY));
				}

				int tempY = pY;
				for (int i = pY + 1; i < 8 && board[pX][i].getPiece() == null; i++) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}
				if (this.coordExists(pX, tempY + 1) && this.coordHasPiece(pX, tempY + 1)
						&& this.pieceIsEnemy(pX, tempY + 1)) {
					posCoords.add(new intPair(pX, tempY + 1));
				}

				tempY = pY;
				for (int i = pY - 1; i >= 0 && board[pX][i].getPiece() == null; i--) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}
				if (this.coordExists(pX, tempY - 1) && this.coordHasPiece(pX, tempY - 1)
						&& this.pieceIsEnemy(pX, tempY - 1)) {
					posCoords.add(new intPair(pX, tempY - 1));
				}

				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Knight) {
				output[0] = board[pX][pY];
				intPair[] posCoords = new intPair[8];
				posCoords[0] = new intPair(pX + 1, pY - 2);
				posCoords[1] = new intPair(pX + 2, pY - 1);
				posCoords[2] = new intPair(pX + 2, pY + 1);
				posCoords[3] = new intPair(pX + 1, pY + 2);
				posCoords[4] = new intPair(pX - 1, pY + 2);
				posCoords[5] = new intPair(pX - 2, pY + 1);
				posCoords[6] = new intPair(pX - 2, pY - 1);
				posCoords[7] = new intPair(pX - 1, pY - 2);
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Bishop) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();

				int tempChange = 0;
				for (int i = 1; pX + i < 8 && pY + i < 8 && board[pX + i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY + tempChange));
				}
				if (this.coordExists(pX + tempChange + 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY + tempChange + 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY - i >= 0 && board[pX - i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY - tempChange));
				}
				if (this.coordExists(pX - tempChange - 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX + i < 8 && pY - i >= 0 && board[pX + i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY - tempChange));
				}
				if (this.coordExists(pX + tempChange + 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY + i < 8 && board[pX - i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY + tempChange));
				}
				if (this.coordExists(pX - tempChange - 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY + tempChange + 1));
				}

				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}

			} else if (p instanceof Queen) {
				output[0] = board[pX][pY];
				ArrayList<intPair> posCoords = new ArrayList<intPair>();

				// Rook half
				int tempX = pX;
				for (int i = pX + 1; i < 8 && board[i][pY].getPiece() == null; i++) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}
				if (this.coordExists(tempX + 1, pY) && this.coordHasPiece(tempX + 1, pY)
						&& this.pieceIsEnemy(tempX + 1, pY)) {
					posCoords.add(new intPair(tempX + 1, pY));
				}

				tempX = pX;
				for (int i = pX - 1; i >= 0 && board[i][pY].getPiece() == null; i--) {
					tempX = i;
				}
				if (tempX != pX) {
					posCoords.add(new intPair(tempX, pY));
				}
				if (this.coordExists(tempX - 1, pY) && this.coordHasPiece(tempX - 1, pY)
						&& this.pieceIsEnemy(tempX - 1, pY)) {
					posCoords.add(new intPair(tempX - 1, pY));
				}

				int tempY = pY;
				for (int i = pY + 1; i < 8 && board[pX][i].getPiece() == null; i++) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}
				if (this.coordExists(pX, tempY + 1) && this.coordHasPiece(pX, tempY + 1)
						&& this.pieceIsEnemy(pX, tempY + 1)) {
					posCoords.add(new intPair(pX, tempY + 1));
				}

				tempY = pY;
				for (int i = pY - 1; i >= 0 && board[pX][i].getPiece() == null; i--) {
					tempY = i;
				}
				if (tempY != pY) {
					posCoords.add(new intPair(pX, tempY));
				}
				if (this.coordExists(pX, tempY - 1) && this.coordHasPiece(pX, tempY - 1)
						&& this.pieceIsEnemy(pX, tempY - 1)) {
					posCoords.add(new intPair(pX, tempY - 1));
				}

				// Bishop half
				int tempChange = 0;
				for (int i = 1; pX + i < 8 && pY + i < 8 && board[pX + i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY + tempChange));
				}
				if (this.coordExists(pX + tempChange + 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY + tempChange + 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY - i >= 0 && board[pX - i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY - tempChange));
				}
				if (this.coordExists(pX - tempChange - 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX + i < 8 && pY - i >= 0 && board[pX + i][pY - i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX + tempChange, pY - tempChange));
				}
				if (this.coordExists(pX + tempChange + 1, pY - tempChange - 1)
						&& this.coordHasPiece(pX + tempChange + 1, pY - tempChange - 1)
						&& this.pieceIsEnemy(pX + tempChange + 1, pY - tempChange - 1)) {
					posCoords.add(new intPair(pX + tempChange + 1, pY - tempChange - 1));
				}

				tempChange = 0;
				for (int i = 1; pX - i >= 0 && pY + i < 8 && board[pX - i][pY + i].getPiece() == null; i++) {
					tempChange = i;
				}
				if (tempChange != 0) {
					posCoords.add(new intPair(pX - tempChange, pY + tempChange));
				}
				if (this.coordExists(pX - tempChange - 1, pY + tempChange + 1)
						&& this.coordHasPiece(pX - tempChange - 1, pY + tempChange + 1)
						&& this.pieceIsEnemy(pX - tempChange - 1, pY + tempChange + 1)) {
					posCoords.add(new intPair(pX - tempChange - 1, pY + tempChange + 1));
				}

				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
				for (intPair i : posCoords) {
					tempX = i.getInt1();
					tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
			} else {
				output[0] = board[pX][pY];
				intPair[] posCoords = new intPair[8];
				posCoords[0] = new intPair(pX, pY - 1);
				posCoords[1] = new intPair(pX + 1, pY - 1);
				posCoords[2] = new intPair(pX + 1, pY);
				posCoords[3] = new intPair(pX + 1, pY + 1);
				posCoords[4] = new intPair(pX, pY + 1);
				posCoords[5] = new intPair(pX - 1, pY + 1);
				posCoords[6] = new intPair(pX - 1, pY);
				posCoords[7] = new intPair(pX - 1, pY - 1);
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
				for (intPair i : posCoords) {
					int tempX = i.getInt1();
					int tempY = i.getInt2();
					if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY)) {
						output[1] = board[tempX][tempY];
						return output;
					}
				}
			}

		}

		return null;
	}

	public void updateArrayList() {
		myPieces = new ArrayList<Piece>();
		for (DraggablePiece dp : piecesOnBoard) {
			myPieces.add(dp.getMyPiece());
		}
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

	public boolean canBeJumped(int pX, int pY) {
		ArrayList<intPair> posCoords = new ArrayList<intPair>();
		System.out.println("pX: " + pX + ", pY: " + pY);
		// Rook half
		int tempX = pX;
		for (int i = pX + 1; i < 8 && board[i][pY].getPiece() == null; i++) {
			tempX = i;
		}
		if (this.coordExists(tempX + 1, pY) && this.coordHasPiece(tempX + 1, pY) && this.pieceIsEnemy(tempX + 1, pY)) {
			posCoords.add(new intPair(tempX + 1, pY));
		}

		tempX = pX;
		for (int i = pX - 1; i >= 0 && board[i][pY].getPiece() == null; i--) {
			tempX = i;
		}
		if (this.coordExists(tempX - 1, pY) && this.coordHasPiece(tempX - 1, pY) && this.pieceIsEnemy(tempX - 1, pY)) {
			posCoords.add(new intPair(tempX - 1, pY));
		}

		int tempY = pY;
		for (int i = pY + 1; i < 8 && board[pX][i].getPiece() == null; i++) {
			System.out.println("i: " + i);
			tempY = i;
		}
		if (this.coordExists(pX, tempY + 1) && this.coordHasPiece(pX, tempY + 1) && this.pieceIsEnemy(pX, tempY + 1)) {
			posCoords.add(new intPair(pX, tempY + 1));
		}

		tempY = pY;
		for (int i = pY - 1; i >= 0 && board[pX][i].getPiece() == null; i--) {
			tempY = i;
		}
		if (this.coordExists(pX, tempY - 1) && this.coordHasPiece(pX, tempY - 1) && this.pieceIsEnemy(pX, tempY - 1)) {
			posCoords.add(new intPair(pX, tempY - 1));
		}

		// Bishop half
		int tempChange = 0;
		for (int i = 1; pX + i < 8 && pY + i < 8 && board[pX + i][pY + i].getPiece() == null; i++) {
			tempChange = i;
		}
		if (this.coordExists(pX + tempChange + 1, pY + tempChange + 1)
				&& this.coordHasPiece(pX + tempChange + 1, pY + tempChange + 1)
				&& this.pieceIsEnemy(pX + tempChange + 1, pY + tempChange + 1)) {
			posCoords.add(new intPair(pX + tempChange + 1, pY + tempChange + 1));
		}

		tempChange = 0;
		for (int i = 1; pX - i >= 0 && pY - i >= 0 && board[pX - i][pY - i].getPiece() == null; i++) {
			tempChange = i;
		}
		if (this.coordExists(pX - tempChange - 1, pY - tempChange - 1)
				&& this.coordHasPiece(pX - tempChange - 1, pY - tempChange - 1)
				&& this.pieceIsEnemy(pX - tempChange - 1, pY - tempChange - 1)) {
			posCoords.add(new intPair(pX - tempChange - 1, pY - tempChange - 1));
		}

		tempChange = 0;
		for (int i = 1; pX + i < 8 && pY - i >= 0 && board[pX + i][pY - i].getPiece() == null; i++) {
			tempChange = i;
		}
		if (this.coordExists(pX + tempChange + 1, pY - tempChange - 1)
				&& this.coordHasPiece(pX + tempChange + 1, pY - tempChange - 1)
				&& this.pieceIsEnemy(pX + tempChange + 1, pY - tempChange - 1)) {
			posCoords.add(new intPair(pX + tempChange + 1, pY - tempChange - 1));
		}

		tempChange = 0;
		for (int i = 1; pX - i >= 0 && pY + i < 8 && board[pX - i][pY + i].getPiece() == null; i++) {
			tempChange = i;
		}
		if (this.coordExists(pX - tempChange - 1, pY + tempChange + 1)
				&& this.coordHasPiece(pX - tempChange - 1, pY + tempChange + 1)
				&& this.pieceIsEnemy(pX - tempChange - 1, pY + tempChange + 1)) {
			posCoords.add(new intPair(pX - tempChange - 1, pY + tempChange + 1));
		}

		intPair[] knightCoords = new intPair[8];
		knightCoords[0] = new intPair(pX + 1, pY - 2);
		knightCoords[1] = new intPair(pX + 2, pY - 1);
		knightCoords[2] = new intPair(pX + 2, pY + 1);
		knightCoords[3] = new intPair(pX + 1, pY + 2);
		knightCoords[4] = new intPair(pX - 1, pY + 2);
		knightCoords[5] = new intPair(pX - 2, pY + 1);
		knightCoords[6] = new intPair(pX - 2, pY - 1);
		knightCoords[7] = new intPair(pX - 1, pY - 2);

		intPair[] pawnCoords = new intPair[2];
		pawnCoords[0] = new intPair(pX + 1, pY + 1);
		pawnCoords[1] = new intPair(pX - 1, pY + 1);

		for (intPair i : knightCoords) {
			if (this.coordExists(i.getInt1(), i.getInt2()) && this.coordHasPiece(i.getInt1(), i.getInt2())
					&& this.pieceIsEnemy(i.getInt1(), i.getInt2())
					&& board[i.getInt1()][i.getInt2()].getPiece() instanceof Knight) {
				posCoords.add(new intPair(i.getInt1(), i.getInt2()));
			}
		}
		for (intPair i : pawnCoords) {
			if (this.coordExists(i.getInt1(), i.getInt2()) && this.coordHasPiece(i.getInt1(), i.getInt2())
					&& this.pieceIsEnemy(i.getInt1(), i.getInt2())
					&& board[i.getInt1()][i.getInt2()].getPiece() instanceof Pawn) {
				posCoords.add(new intPair(i.getInt1(), i.getInt2()));
			}
		}
		
		
		ArrayList<intPair> kingCoords = new ArrayList<intPair>();
		kingCoords.add(new intPair(pX, pY - 1));
		kingCoords.add(new intPair(pX + 1, pY - 1));
		kingCoords.add(new intPair(pX + 1, pY));
		kingCoords.add(new intPair(pX + 1, pY + 1));
		kingCoords.add(new intPair(pX, pY + 1));
		kingCoords.add(new intPair(pX - 1, pY + 1));
		kingCoords.add(new intPair(pX - 1, pY));
		kingCoords.add(new intPair(pX - 1, pY - 1));
		
		for (intPair i : kingCoords) {
			if (this.coordExists(i.getInt1(), i.getInt2()) && this.coordHasPiece(i.getInt1(), i.getInt2())
					&& this.pieceIsEnemy(i.getInt1(), i.getInt2())
					&& board[i.getInt1()][i.getInt2()].getPiece() instanceof King) {
				posCoords.add(new intPair(i.getInt1(), i.getInt2()));
			}
		}
		
		for (intPair i : posCoords) {
			int jumpingX = i.getInt1();
			int jumpingY = i.getInt2();
			Piece jumpingPiece = board[jumpingX][jumpingY].getPiece();
			if (jumpingX - pX == 0 || jumpingY - pY == 0) {
				if (jumpingPiece instanceof Rook || jumpingPiece instanceof Queen) {
					return true;
				}
			}
			if (Math.abs(jumpingX - pX) == Math.abs(jumpingY - pY)) {
				if (Math.abs(jumpingX - pX) == 1 && jumpingY - pY == 1) {
					if (jumpingPiece instanceof Bishop || jumpingPiece instanceof Queen
							|| jumpingPiece instanceof Pawn) {
						return true;
					}
				}
				if (jumpingPiece instanceof Bishop || jumpingPiece instanceof Queen) {
					return true;
				}
			}
			if ((Math.abs(jumpingX - pX) == 1 && Math.abs(jumpingY - pY) == 2)
					|| (Math.abs(jumpingX - pX) == 2 && Math.abs(jumpingY - pY) == 1)) {
				if (jumpingPiece instanceof Knight) {
					return true;
				}
			}
			if ((Math.abs(jumpingX - pX) <= 1 && Math.abs(jumpingY - pY) <= 1)) {
				if (jumpingPiece instanceof King) {
					return true;
				}
			}

		}
		return false;
	}

	public int checkNetGain(Piece p1, int targX, int targY) {
		int output = 0;
		if (coordHasPiece(targX, targY) && this.pieceIsEnemy(targX, targY)) {
			output = board[targX][targY].getPiece().getValue();
		}
		if (canBeJumped(targX, targY)) {
			output = output - p1.getValue();
		}
		if (canBeJumped(p1.getX(), p1.getY())) {
			output = output + p1.getValue();
		}
		return output;
	}

	public boolean kingCanBeJumped() {
		this.updateArrayList();
		for (Piece p : myPieces) {
			if (p instanceof King) {
				if (canBeJumped(p.getX(), p.getY())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public Tile[] moveKingToSafety() {
		this.updateArrayList();
		Tile[] output = null;
		King k = null;
		for (Piece p : myPieces) {
			if (p instanceof King) {
				k = (King) p;
			}
		}

		int pX = k.getX();
		int pY = k.getY();
		ArrayList<intPair> posCoords = new ArrayList<intPair>();
		posCoords.add(new intPair(pX, pY - 1));
		posCoords.add(new intPair(pX + 1, pY - 1));
		posCoords.add(new intPair(pX + 1, pY));
		posCoords.add(new intPair(pX + 1, pY + 1));
		posCoords.add(new intPair(pX, pY + 1));
		posCoords.add(new intPair(pX - 1, pY + 1));
		posCoords.add(new intPair(pX - 1, pY));
		posCoords.add(new intPair(pX - 1, pY - 1));

		Collections.shuffle(posCoords);
		for (intPair i : posCoords) {
			int tempX = i.getInt1();
			int tempY = i.getInt2();
			if (coordExists(tempX, tempY) && coordHasPiece(tempX, tempY) && pieceIsEnemy(tempX, tempY) && !canBeJumped(tempX, tempY)) {
				output = new Tile[2];
				output[0] = board[pX][pY];
				output[1] = board[tempX][tempY];
				return output;
			}
		}
		for (intPair i : posCoords) {
			int tempX = i.getInt1();
			int tempY = i.getInt2();
			if (coordExists(tempX, tempY) && !coordHasPiece(tempX, tempY) && !canBeJumped(tempX, tempY)) {
				output = new Tile[2];
				output[0] = board[pX][pY];
				output[1] = board[tempX][tempY];
				return output;
			}
		}

		return output;
	}
}

// Needs a method that finds the best jump for that turn
// A method for protecting a king in check
// Check for move ability to move rooks, bishops, and queens less than maximum
// spaces available



