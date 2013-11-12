package ui;

import model.Move;
import model.PlayerColor;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Color;

public class BoardFigure extends Figure {

	private BoardSquare[][] squares;
	private Move move = null;

	public void init() {
		setBorder(new LineBorder());

		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.numColumns = 8;
		gridLayout.makeColumnsEqualWidth = true;

		setLayoutManager(gridLayout);

		squares = new BoardSquare[8][];

		for (int i = 0; i < 8; i++) {
			squares[i] = new BoardSquare[8];

			for (int j = 0; j < 8; j++) {
				BoardSquare square = new BoardSquare(i, j);
				squares[i][j] = square;
				add(square);

				if ((i == 3 || i == 4) && (j == 3 || j == 4)) {
					square.placePiece((i == j) ? ColorConstants.white
							: ColorConstants.black);
				} else {
					square.addMouseListener(new MouseListener.Stub() {
						@Override
						public void mousePressed(MouseEvent event) {
							BoardSquare selectedShape = (BoardSquare) event
									.getSource();

							synchronized (BoardFigure.this) {
								move = new Move(selectedShape.getRow(),
										selectedShape.getColumn());

								BoardFigure.this.notifyAll();
							}
						}
					});
				}
			}
		}

	}

	public void update(PlayerColor[][] boardTable) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (boardTable[i][j] == null) {
					continue;
				}

				if (!squares[i][j].isPiecePlaced()) {
					squares[i][j].placePiece(getColor(boardTable[i][j]));
				} else if (squares[i][j].getPieceColor() != getColor(boardTable[i][j])) {
					squares[i][j].toggleColor();
				}
			}
		}
	}

	public void refresh(int row, int col, PlayerColor color) {
		squares[row][col].placePiece(getColor(color));
	}

	public Color getColor(PlayerColor color) {
		return color == PlayerColor.BLACK ? ColorConstants.black: ColorConstants.white;
	}

	public Move getMove() {
		return move;
	}

	public void resetMove() {
		move = null;
	}

}
