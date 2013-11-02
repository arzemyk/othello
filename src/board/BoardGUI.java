package board;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BoardGUI {
	public static void main(String[] args) {

		Display d = new Display();
		Shell shell = new Shell(d);
		shell.setLayout(new org.eclipse.swt.layout.GridLayout(2, false));
		shell.setMinimumSize(700, 500);

		FigureCanvas canvas = new FigureCanvas(shell);
		Figure container = new Figure();
		container.setBorder(new LineBorder());

		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.numColumns = 8;
		gridLayout.makeColumnsEqualWidth = true;

		container.setLayoutManager(gridLayout);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Shape shape = new RectangleFigure();
				shape.setSize(50, 50);
				container.add(shape);
				
				if ((i ==3 || i == 4) && (j == 3 || j == 4)) {
					Ellipse circle = new Ellipse();
					circle.setSize(50, 50);
					if (i == j) {
						circle.setBackgroundColor(ColorConstants.white);
					} else {
						circle.setBackgroundColor(ColorConstants.black);
					}
					shape.add(circle);
				}
			}
		}

		canvas.setContents(container);

		shell.setText("Draw2d");
		shell.setSize(200, 100);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();

	}
}
