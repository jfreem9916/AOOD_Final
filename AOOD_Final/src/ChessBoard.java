import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

	private DraggablePiece currentPiece;

	public ChessBoard() {
		super("Chess");

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
					Tile newTile = new Tile(new Color(242, 240, 215));
					board[i][j] = newTile;
					table.add(newTile);
					newTile.setBounds(i * tileSize, j * tileSize, tileSize, tileSize);

				} else {
					Tile newTile = new Tile(new Color(147, 88, 28));
					board[i][j] = newTile;
					table.add(newTile);
					newTile.setBounds(i * tileSize, j * tileSize, tileSize, tileSize);
				}
			}
		}

		playerPieces = new ArrayList<DraggablePiece>();
		cpuPieces = new ArrayList<DraggablePiece>();

		currentPiece = null;

		this.addNewPiece('P', 'W', 0, 7);
		this.addNewPiece('P', 'B', 1, 7);

	}

	public void addNewPiece(char type, char color, int x, int y) {
		Piece p = null;
		if (type == 'P') {
			p = new Pawn(x, y, color);
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
		board[x][y].setPiece(dragPiece.getMyPiece());
		table.add(dragPiece);
		dragPiece.setBounds(85 * x, 85 * y, 85, 85);
		dragPiece.addMouseListener(new PieceClick(dragPiece));
		dragPiece.addMouseMotionListener(new PieceDrag());

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

		Tile newTile = board[iIndex][jIndex];
		Tile oldTile = board[p.getMyPiece().getX()][p.getMyPiece().getY()];

		if (!newTile.isEmpty()) {
			p.setBounds(p.getMyPiece().getX() * 85, p.getMyPiece().getY() * 85, 85, 85);
			return;
		}

		if (isValidMove(p.getMyPiece(), newTile, iIndex, jIndex)) {
			newTile.setPiece(oldTile.getPiece());
			oldTile.setPiece(null);
			p.setBounds(newTile.getBounds());
			p.getMyPiece().setX(iIndex);
			p.getMyPiece().setY(jIndex);
		} else {

		}

	}

	public boolean isValidMove(Piece p, Tile targetTile, int targX, int targY) {
		boolean output = true;
		output = output && p.canReachTile(targX, targY);
		return output;
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
