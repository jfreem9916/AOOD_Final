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

		UIManager.put("Label.background", myBrown);
		UIManager.put("Label.foreground", myWhite);
		UIManager.put("Label.font", myFont);

		width = 696;
		height = 718;
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

		cpu = new CPU(board);

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
		} else {
			cpuPieces.add(dragPiece);
		}
		dragPiece.addMouseListener(new PieceClick(dragPiece));
		dragPiece.addMouseMotionListener(new PieceDrag());
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
				if(jumpedPiece != null){
					cpuPieces.remove(jumpedPiece);
				}
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
			newTile.setPiece(oldTile.getPiece());
			oldTile.setPiece(null);

			p.setBounds(newTile.getBounds());
			p.getMyPiece().setX(iIndex);
			p.getMyPiece().setY(jIndex);
			if (jumpedPiece != null) {
				if (jumpedPiece.getMyPiece() instanceof King) {
					if (jumpedPiece.getMyPiece().getColor() == 'B') {
						JOptionPane.showMessageDialog(null, "The winner is you!", "Winner",
								JOptionPane.INFORMATION_MESSAGE);

					} else {
						JOptionPane.showMessageDialog(null, "The winner is the computer!", "Winner",
								JOptionPane.INFORMATION_MESSAGE);

					}
					System.exit(0);
				}
			}

			if (p.getMyPiece() instanceof Pawn) {
				((Pawn) p.getMyPiece()).moved();
				if (p.getMyPiece().y == 0 && p.getMyPiece().getColor() == 'W') {
					this.promotePiece(p);
				}
				if (p.getMyPiece().y == 7 && p.getMyPiece().getColor() == 'B') {
					this.promotePiece(p);
				}
			}

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

	private void promotePiece(DraggablePiece dp) {
		char newType = 'Q';
		Piece p = dp.getMyPiece();
		Piece newPiece = null;
		if (newType == 'Q') {
			newPiece = new Queen(p.getX(), p.getY(), p.getColor());
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
