package ui;

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
								selectedShape.placePiece(getColor(GameState.HUMAN));
								Game.humanPlayer.makeMove(selectedShape.getRow(), selectedShape.getColumn());
							}
						}
					});
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
