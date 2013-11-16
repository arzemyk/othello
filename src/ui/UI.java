package ui;

import model.Board;
import model.Move;
import model.PlayerColor;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class UI extends Thread {
	private Display display;
	private Shell shell;
	private BoardFigure boardFigure;
	private Board board;
	private Label gameStateLabel;
	private Label humanScoreLabel;
	private Label computerLabel;

	public UI(Board board) {
		this.board = board;
	}
	
	@Override
	public void run() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		shell.setMinimumSize(700, 500);

		FigureCanvas canvas = new FigureCanvas(shell);
		boardFigure = new BoardFigure();
		boardFigure.init();
		canvas.setContents(boardFigure);

		shell.setText("Othello");
		shell.setSize(250, 100);
		
		Composite c = new Composite(shell, SWT.None);
		c.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
		c.setLayout(new GridLayout(1, true));
		
		humanScoreLabel = new Label(c, SWT.NONE);
		computerLabel = new Label(c, SWT.NONE);
		gameStateLabel = new Label(c, SWT.NONE);
		gameStateLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
		humanScoreLabel.setText("Black score: " + board.getBlackScores());
		humanScoreLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
		computerLabel.setText("White score: " + board.getWhiteScores());
		computerLabel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, true));
		
		shell.open();

		while (!shell.isDisposed())
			while (!display.readAndDispatch())
				display.sleep();
		
		display.dispose();
	}

	public void update() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				boardFigure.update(board.getBoardTable());
				humanScoreLabel.setText("Black score: " + board.getBlackScores());
				computerLabel.setText("White score: " + board.getWhiteScores());
				
				switch (board.getGameState()) {
				
				case RUNNING:
					switch (board.getCurrentPlayerColor()) {
					case BLACK:
						gameStateLabel.setText("Black's turn");
						break;
					
					case WHITE:
						gameStateLabel.setText("White's turn");
						break;
					}
				break;
				
				case BLACK_WON:
					gameStateLabel.setText("Black won");
					break;
				
				case WHITE_WON:
					gameStateLabel.setText("White won");
					break;
				
				case DRAW:
					gameStateLabel.setText("Draw");
					break;
				}
				
				//shell.layout();
			}
		});
	}
	
	
	public Move getMove(PlayerColor playerColor) {
		
		synchronized (boardFigure) {
			do {
				boardFigure.resetMove();
				
				while (boardFigure.getMove() == null) {
					try {
						boardFigure.wait();
					} catch (InterruptedException e) {
						
					}
				}
			} while (!board.getLegalMoves().contains(boardFigure.getMove()));
			
			return boardFigure.getMove();
		}
	}
}
