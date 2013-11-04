package ui;

import model.Board;
import model.Game;
import model.GameState;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Color;

public class BoardFigure extends Figure {

	private BoardSquare[][] squares;
	private Board board;
	
	public BoardFigure(Board board) {
		this.board = board;
	}
	
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
							if (Game.state == GameState.HUMAN) {
								BoardSquare selectedShape = (BoardSquare) event
										.getSource();
								if (Game.humanPlayer.makeMove(selectedShape.getRow(), selectedShape.getColumn())) {
									update();
								}
							}
						}
					});
				}
			}
		}

	}

	public void update() {
		GameState[][] boardTable = board.getBoardTable();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (boardTable[i][j] == GameState.NONE) {
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
	
	public void refresh(int row, int col, GameState color) {
		squares[row][col].placePiece(getColor(color));
	}

	public Color getColor(GameState color) {
		return (color == GameState.HUMAN) ? ColorConstants.white
				: ColorConstants.black;
	}
}
