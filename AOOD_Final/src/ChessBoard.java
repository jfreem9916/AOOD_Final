import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ChessBoard extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5656815955239111516L;
	private Tile[][] board;
	private JPanel table;
	private int width, height;
	private ArrayList<DraggablePiece> playerPieces;
	private ArrayList<DraggablePiece> cpuPieces;
	private CPU cpu;
	private DraggablePiece currentPiece;

	public ChessBoard() {
		super("Chess");
		this.setResizable(false);
		Font myFont = new Font("Apple Chancery", Font.PLAIN, 16);
		Color myBrown = new Color(147, 88, 28);
		Color myWhite = new Color(242, 240, 215);

		UIManager.put("OptionPane.background", myBrown);
		UIManager.put("OptionPane.messageForeground", myWhite);
		UIManager.put("OptionPane.messageDialogTitle", myBrown);
		UIManager.put("Panel.background", myBrown);
		UIManager.put("OptionPane.messageFont", myFont);
		UIManager.put("Button.background", myBrown);
		UIManager.put("Button.foreground", myWhite);
		UIManager.put("Button.select", myWhite);
		UIManager.put("Button.font", myFont);
		UIManager.put("Menu.selectionBackground", myBrown);
		UIManager.put("Menu.selectionForeground", myBrown);
		UIManager.put("MenuItem.selectionBackground", myWhite);
		UIManager.put("MenuItem.selectionForeground", myBrown);
		UIManager.put("MenuItem.font", myFont);
		UIManager.put("Menu.font", myFont);
		UIManager.put("TextField.background", myWhite);
		UIManager.put("TextField.foreground", Color.BLACK);
		UIManager.put("TextField.font", myFont);
		
		UIManager.put("Label.background", myBrown);
		UIManager.put("Label.foreground", myWhite);
		UIManager.put("Label.font", myFont);

		width = 686;
		height = 708;
		int tileSize = 85;

		this.setSize(width, height);
		this.setLocation((1600 - width) / 2, (900 - height) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		table = new JPanel();
		this.add(table);
		table.setLayout(null);

		board = new Tile[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (j % 2 == i % 2) {
					Tile newTile = new Tile(myWhite);
					board[i][j] = newTile;
					table.add(newTile);
					newTile.setBounds(i * tileSize, j * tileSize, tileSize, tileSize);

				} else {
					Tile newTile = new Tile(myBrown);
					board[i][j] = newTile;
					table.add(newTile);
					newTile.setBounds(i * tileSize, j * tileSize, tileSize, tileSize);
				}
			}
		}

		playerPieces = new ArrayList<DraggablePiece>();
		cpuPieces = new ArrayList<DraggablePiece>();

		currentPiece = null;

		this.addNewPiece('P', 'W', 0, 6);
		this.addNewPiece('P', 'W', 1, 6);
		this.addNewPiece('P', 'W', 2, 6);
		this.addNewPiece('P', 'W', 3, 6);
		this.addNewPiece('P', 'W', 4, 6);
		this.addNewPiece('P', 'W', 5, 6);
		this.addNewPiece('P', 'W', 6, 6);
		this.addNewPiece('P', 'W', 7, 6);
		this.addNewPiece('P', 'W', 0, 6);

		this.addNewPiece('R', 'W', 0, 7);
		this.addNewPiece('R', 'W', 7, 7);

		this.addNewPiece('N', 'W', 1, 7);
		this.addNewPiece('N', 'W', 6, 7);

		this.addNewPiece('B', 'W', 2, 7);
		this.addNewPiece('B', 'W', 5, 7);

		this.addNewPiece('K', 'W', 4, 7);
		this.addNewPiece('Q', 'W', 3, 7);

		this.addNewPiece('P', 'B', 0, 1);
		this.addNewPiece('P', 'B', 1, 1);
		this.addNewPiece('P', 'B', 2, 1);
		this.addNewPiece('P', 'B', 3, 1);
		this.addNewPiece('P', 'B', 4, 1);
		this.addNewPiece('P', 'B', 5, 1);
		this.addNewPiece('P', 'B', 6, 1);
		this.addNewPiece('P', 'B', 7, 1);

		this.addNewPiece('R', 'B', 0, 0);
		this.addNewPiece('R', 'B', 7, 0);

		this.addNewPiece('N', 'B', 1, 0);
		this.addNewPiece('N', 'B', 6, 0);

		this.addNewPiece('B', 'B', 2, 0);
		this.addNewPiece('B', 'B', 5, 0);

		this.addNewPiece('K', 'B', 4, 0);
		this.addNewPiece('Q', 'B', 3, 0);

		cpu = new CPU(board, cpuPieces);

		this.repaintAll();

	}

	public void addNewPiece(char type, char color, int x, int y) {
		Piece p = null;
		if (type == 'P') {
			p = new Pawn(x, y, color);
		} else if (type == 'R') {
			p = new Rook(x, y, color);
		} else if (type == 'K') {
			p = new King(x, y, color);
		} else if (type == 'B') {
			p = new Bishop(x, y, color);
		} else if (type == 'Q') {
			p = new Queen(x, y, color);
		} else if (type == 'N') {
			p = new Knight(x, y, color);
		}

		if (board[x][y].getPiece() != null || p == null) {
			return;
		}

		DraggablePiece dragPiece = new DraggablePiece(p);

		if (color == 'W') {

			playerPieces.add(dragPiece);
			dragPiece.addMouseListener(new PieceClick(dragPiece));
			dragPiece.addMouseMotionListener(new PieceDrag());
		} else {
			cpuPieces.add(dragPiece);
		}

		board[x][y].setPiece(dragPiece.getMyPiece());
		table.add(dragPiece);
		dragPiece.setBounds(85 * x, 85 * y, 85, 85);

		reorderComponents(dragPiece);
		this.repaintAll();

	}

	public void dropPiece(DraggablePiece p) {
		int iIndex = -1;
		int jIndex = -1;
		double maxTouch = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Rectangle computedIntersection = SwingUtilities.computeIntersection(p.getX(), p.getY(), p.getWidth(),
						p.getHeight(), board[i][j].getBounds());
				double area = computedIntersection.getWidth() * computedIntersection.getHeight();
				if (area > maxTouch) {
					iIndex = i;
					jIndex = j;
					maxTouch = area;
				}
			}
		}

		if (iIndex == -1 || jIndex == -1 || maxTouch < 1000) {
			p.setBounds(p.getMyPiece().getX() * 85, p.getMyPiece().getY() * 85, 85, 85);
			return;
		}
		DraggablePiece jumpedPiece = null;

		Tile newTile = board[iIndex][jIndex];
		Tile oldTile = board[p.getMyPiece().getX()][p.getMyPiece().getY()];
		if (oldTile.equalsTile(newTile)) {
			p.setBounds(p.getMyPiece().getX() * 85, p.getMyPiece().getY() * 85, 85, 85);
			return;
		}

		if (isValidMove(p.getMyPiece(), newTile, iIndex, jIndex)) {

			if (!newTile.isEmpty()) {
				for (DraggablePiece dp : cpuPieces) {
					if (dp.getBounds().getX() == newTile.getBounds().getX()
							&& dp.getBounds().getY() == newTile.getBounds().getY()) {
						jumpedPiece = dp;
					}
				}

				if (jumpedPiece != null) {
					cpuPieces.remove(jumpedPiece);
					table.remove(jumpedPiece);

				}
			}
			newTile.setPiece(oldTile.getPiece());
			oldTile.setPiece(null);

			p.setBounds(newTile.getBounds());
			p.getMyPiece().setX(iIndex);
			p.getMyPiece().setY(jIndex);
			if (jumpedPiece != null) {
				if (jumpedPiece.getMyPiece() instanceof King || cpuPieces.size() == 1) {
					JOptionPane.showMessageDialog(null, "You have won!", "Winner",
							JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
			

			if (p.getMyPiece() instanceof Pawn) {
				((Pawn) p.getMyPiece()).moved();
				if (p.getMyPiece().y == 0 && p.getMyPiece().getColor() == 'W') {
					this.promotePiece(p);
				}
			}

			this.cpuTurn(cpu.decideMove());

		} else {
			p.setBounds(oldTile.getBounds());
		}

	}

	public boolean isValidMove(Piece p, Tile targetTile, int targX, int targY) {
		boolean tileFilled = false;
		char c = ' ';
		if (!targetTile.isEmpty()) {
			tileFilled = true;
			c = targetTile.getPiece().getColor();
		}

		boolean output = true;
		output = output && p.canReachTile(targX, targY, tileFilled, c) && this.pathClear(p, targX, targY);

		return output;
	}

	private boolean pathClear(Piece p, int targX, int targY) {
		if (p instanceof Rook) {
			int stepVal = 0;

			if (p.getX() != targX) {
				if (p.getX() > targX) {
					stepVal = -1;
				} else if (p.getX() < targX) {
					stepVal = 1;
				}
				for (int i = p.getX() + stepVal; i != targX; i = i + stepVal) {
					if (!this.board[i][targY].isEmpty()) {
						return false;
					}
				}
			} else {
				if (p.getY() > targY) {
					stepVal = -1;
				} else if (p.getY() < targY) {
					stepVal = 1;
				}
				for (int i = p.getY() + stepVal; i != targY; i = i + stepVal) {
					if (!this.board[targX][i].isEmpty()) {
						return false;
					}
				}
			}
		} else if (p instanceof Bishop) {
			int xStepVal = 0;
			int yStepVal = 0;
			if (p.getX() > targX) {
				xStepVal = -1;
			} else if (p.getX() < targX) {
				xStepVal = 1;
			}
			if (p.getY() > targY) {
				yStepVal = -1;
			} else if (p.getY() < targY) {
				yStepVal = 1;
			}
			int tempX = p.getX() + xStepVal;
			int tempY = p.getY() + yStepVal;
			int netChange = Math.abs(p.getX() - targX);
			for (int i = 0; i < netChange - 1; i++) {
				if (!this.board[tempX][tempY].isEmpty()) {
					return false;
				}
				tempX += xStepVal;
				tempY += yStepVal;

			}
		} else if (p instanceof Queen) {
			int xStepVal = 0;
			int yStepVal = 0;
			if (p.getX() > targX) {
				xStepVal = -1;
			} else if (p.getX() < targX) {
				xStepVal = 1;
			}
			if (p.getY() > targY) {
				yStepVal = -1;
			} else if (p.getY() < targY) {
				yStepVal = 1;
			}

			if (xStepVal == 0) {
				for (int i = p.getY() + yStepVal; i != targY; i = i + yStepVal) {
					if (!this.board[targX][i].isEmpty()) {
						return false;
					}
				}
			} else if (yStepVal == 0) {
				for (int i = p.getX() + xStepVal; i != targX; i = i + xStepVal) {
					if (!this.board[i][targY].isEmpty()) {
						return false;
					}
				}
			} else {
				int tempX = p.getX() + xStepVal;
				int tempY = p.getY() + yStepVal;
				int netChange = Math.abs(p.getX() - targX);
				for (int i = 0; i < netChange - 1; i++) {
					if (!this.board[tempX][tempY].isEmpty()) {
						return false;
					}
					tempX += xStepVal;
					tempY += yStepVal;
				}
			}
		}

		return true;
	}

	private void cpuTurn(Tile[] move) {
		DraggablePiece jumpedPiece = null;
		if (move == null) {
			JOptionPane.showMessageDialog(null, "The cpu is out of possible moves. This should not happen.", "Error",
					JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		Tile newTile = move[1];
		Tile oldTile = move[0];
		if (newTile.getPiece() != null) {
			for (DraggablePiece dp : playerPieces) {
				if (dp.getBounds().getX() == newTile.getBounds().getX()
						&& dp.getBounds().getY() == newTile.getBounds().getY()) {
					jumpedPiece = dp;
				}
			}

			if (jumpedPiece != null) {
				playerPieces.remove(jumpedPiece);
				table.remove(jumpedPiece);

			}
		}

		DraggablePiece movedPiece = null;
		for (DraggablePiece dp : cpuPieces) {

			if (dp.getBounds().getX() == oldTile.getBounds().getX()
					&& dp.getBounds().getY() == oldTile.getBounds().getY()) {
				movedPiece = dp;
			}
		}
		movedPiece.setBounds(newTile.getBounds());
		oldTile.setPiece(null);
		newTile.setPiece(movedPiece.getMyPiece());
		movedPiece.getMyPiece().setX((int) (newTile.getBounds().getX() / 85));
		movedPiece.getMyPiece().setY((int) (newTile.getBounds().getY() / 85));

		if (movedPiece.getMyPiece() instanceof Pawn) {
			((Pawn) movedPiece.getMyPiece()).moved();
			if (movedPiece.getMyPiece().y == 7 && movedPiece.getMyPiece().getColor() == 'B') {
				this.promotePiece(movedPiece);
			}
		}

		if (jumpedPiece != null) {
			if (jumpedPiece.getMyPiece() instanceof King || playerPieces.size() == 1) {
				JOptionPane.showMessageDialog(null, "The CPU wins!", "Winner",
						JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}
		
		
		if (this.kingCanBeJumped()) {
			JOptionPane.showMessageDialog(null, "Your king is in check!", "Check",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void promotePiece(DraggablePiece dp) {
		String newType = "";
		Piece p = dp.getMyPiece();
		Piece newPiece = null;
		if(dp.getMyPiece().getColor() == 'B'){
			newType = "Queen";
		}
		while(!newType.trim().equalsIgnoreCase("Queen") && !newType.trim().equalsIgnoreCase("Rook") && !newType.trim().equalsIgnoreCase("Bishop") && !newType.trim().equalsIgnoreCase("Knight")){
			newType = JOptionPane.showInputDialog("Enter the piece you want:");
			if(newType == null){
				newType = "";
			}

		}
		if (newType.trim().equalsIgnoreCase("Queen")) {
			newPiece = new Queen(p.getX(), p.getY(), p.getColor());
		}
		else if(newType.trim().equalsIgnoreCase("Rook")){
			newPiece = new Rook(p.getX(), p.getY(), p.getColor());

		}
		else if(newType.trim().equalsIgnoreCase("Bishop")){
			newPiece = new Bishop(p.getX(), p.getY(), p.getColor());

		}
		else{
			newPiece = new Knight(p.getX(), p.getY(), p.getColor());
		}
		this.board[p.getX()][p.getY()].setPiece(p);
		dp.setMyPiece(newPiece);
		this.repaintAll();

	}

	private void repaintAll() {
		for (DraggablePiece p : playerPieces) {
			p.repaint();
		}
		for (DraggablePiece p : cpuPieces) {
			p.repaint();

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
		if (board[x][y].getPiece().getColor() == 'W') {
			return false;
		}
		return true;
	}
	public boolean canBeJumped(int pX, int pY) {
		ArrayList<intPair> posCoords = new ArrayList<intPair>();
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
		pawnCoords[0] = new intPair(pX + 1, pY - 1);
		pawnCoords[1] = new intPair(pX - 1, pY - 1);

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
				if (Math.abs(jumpingX - pX) == 1 && jumpingY - pY == -1) {
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
	
	
	public boolean kingCanBeJumped() {
		for (DraggablePiece p : playerPieces) {
			if (p.getMyPiece() instanceof King) {
				if (canBeJumped(p.getMyPiece().getX(), p.getMyPiece().getY())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	private void reorderComponents(Component compo) {

		Component[] components = table.getComponents();
		int i = 0;
		int index = -1;
		for (Component c : components) {
			if (c == compo) {
				index = i;
			}
			i++;
		}

		if (index != -1) {
			table.removeAll();
			Component temp = components[index];

			Component[] newComp = new Component[components.length];
			newComp[0] = temp;
			int tempI = 1;
			for (Component c : components) {
				if (c != temp) {
					newComp[tempI] = c;
					tempI++;
				}
			}

			for (Component comp : newComp) {
				table.add(comp);
			}
		}

	}

	private class PieceClick extends MouseAdapter {
		private DraggablePiece p;

		public PieceClick(DraggablePiece p) {
			this.p = p;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			currentPiece = this.p;
			reorderComponents(currentPiece);

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			dropPiece(currentPiece);
			currentPiece = null;

		}

	}

	private class PieceDrag extends MouseMotionAdapter {

		@Override
		public void mouseDragged(MouseEvent e) {
			if (currentPiece != null) {
				int mouseX = currentPiece.getX() + e.getX();
				int mouseY = currentPiece.getY() + e.getY();
				currentPiece.setBounds(mouseX - currentPiece.getWidth() / 2, mouseY - currentPiece.getHeight() / 2,
						currentPiece.getWidth(), currentPiece.getHeight());
			}
		}

	}

}
