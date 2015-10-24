package view;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * <h1>BasicWindow</h1> The BasicWindow abstract class represents our basic window widget,
 * defining our shell and display
 * <p>
 *
 * @author Ariel Rosenfeld,Ofir Calif
 *
 * 
 */

public abstract class BasicWindow extends Observable implements Runnable {

	Display display;
	Shell shell;
	/**
	 * constructor for BasicWindow
	 */
	public BasicWindow(String title, int width, int height) {
		display = new Display();
		shell = new Shell(display);
		shell.setSize(width, height);
		shell.setText(title);
	}
	/**
	 * this method sets the window and initializes our widgets,used as an abstract for now - must be
	 * implemented! 
	 */
	abstract void initWidgets();
	/**
	 * this method runs our shell,display and the widgets
	 */
	@Override
	public void run() {
		initWidgets();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}

