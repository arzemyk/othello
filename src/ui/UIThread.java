package ui;

import model.GameState;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class UIThread extends Thread {
	private Display display;
	private Shell shell;
	private BoardFigure board;

	@Override
	public void run() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new org.eclipse.swt.layout.GridLayout(2, false));
		shell.setMinimumSize(700, 500);

		FigureCanvas canvas = new FigureCanvas(shell);
		board = new BoardFigure();
		board.init();
		canvas.setContents(board);

		shell.setText("Othello");
		shell.setSize(200, 100);

		shell.open();

		while (!shell.isDisposed())
			while (!display.readAndDispatch())
				display.sleep();
	}

	public void update(final int row, final int col, final GameState color) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				board.refresh(row, col, color);
			}
		});
	}
}
