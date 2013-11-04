package ui;

import model.Board;
import model.GameState;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class UIThread extends Thread {
	private Display display;
	private Shell shell;
	private BoardFigure boardFigure;
	private Board board;
	private Label humanScore;
	private Label computerScore;

	@Override
	public void run() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		shell.setMinimumSize(700, 500);

		FigureCanvas canvas = new FigureCanvas(shell);
		boardFigure = new BoardFigure(board);
		boardFigure.init();
		canvas.setContents(boardFigure);

		shell.setText("Othello");
		shell.setSize(250, 100);

		Composite c = new Composite(shell, SWT.None);
		c.setLayout(new GridLayout(1, true));
		
		humanScore = new Label(c, SWT.NONE);
		computerScore = new Label(c, SWT.NONE);
		humanScore.setText("Human score: " + board.humanScore);
		computerScore.setText("Computer score: " + board.computerScore);
		shell.open();

		while (!shell.isDisposed())
			while (!display.readAndDispatch())
				display.sleep();
		
		display.dispose();
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void update() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				boardFigure.update();
				humanScore.setText("Human score: " + board.humanScore);
				computerScore.setText("Computer score: " + board.computerScore);
				shell.layout();
			}
		});
	}
	
	public void update(final int row, final int col, final GameState color) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				boardFigure.refresh(row, col, color);
			}
		});
	}
}
