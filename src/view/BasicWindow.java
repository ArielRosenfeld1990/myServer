package view;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * <h1>BasicWindow</h1> The BasicWindow abstract class represents our basic window widget,
 * defining our shell and display
 * <p>
 *
 * @author Ariel Rosenfeld and Ofir Calif
 *
 * 
 */

public abstract class BasicWindow extends Observable implements Runnable {

	Display display;
	Shell shell;   
/**
 * <h1>BasicWindow constructor</h1>
 * BasicWindow is our constructor to the class, initiliazing the display and shell data members
 * @param title is the name to the BasicWindow
 * @param width is the width of the window
 * @param height is the height of the window
 */
	public BasicWindow(String title, int width, int height) {
		display = new Display();
		shell = new Shell(display);
		shell.setSize(width, height);
		shell.setText(title);
	}
	/**
	 * <h1>initWidgets</h1>
	 * initWidgets method sets the window and initializes our widgets,used as an abstract for now - must be
	 * implemented! 
	 */
	abstract void initWidgets();
	/**
	 * <h1>run</h1>
	 * run method runs our shell,display and the widgets
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

