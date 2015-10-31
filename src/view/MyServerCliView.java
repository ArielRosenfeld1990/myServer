package view;

import java.util.Observable;
import java.util.Observer;
/**
 * <h1>MyServerCliView</h1>
 * MyServerCliView class implements our View interface,Observing the ServerCLI class
 * and is Observable so it can notify its Observer when a change occurred
 * @author Ariel Rosenfeld,Ofir Calif
 *
 */
public class MyServerCliView extends Observable implements Observer, View {
    ServerCLI ui;
    /**
     * <h1>start</h1>
     * start method is overriding the start method in the View interface 
     * for its specific use
     */
	@Override
	public void start() {
		ui = new ServerCLI(System.in, System.out);
		ui.addObserver(this);
		ui.start();  

	}
    /**
     * <h1>stop</h1>
     * stop method is overriding the stop method in the View interface
     * for its specific use
     */
	@Override
	public void stop() {
		ui.close();

	}
    /**
     * <h1>update</h1>
     * update method is overriding the update method in the Observer interface,
     * if a change occurred in the CLI, it notifies the Observers 
     */
	@Override
	public void update(Observable o, Object arg) {
		if (o == ui) {
			setChanged();
			notifyObservers();
		}
 
	}
    /**
     * <h1>display</h1> 
     * display method is overriding the display method in the View interface,
     * responsible for displaying the obj on the CLI
     */
	@Override
	public void display(Object obj) {
		if (obj != null) {
			switch (obj.getClass().getSimpleName()) {
			case "String":
				ui.display((String)obj);
				break;
			default: 
				break;
			}
		}
	}
    /**
     * <h1>getUserCommand</h1>
     * getUserCommand overriding the getUserCommand method in the View interface
     * for its specific use
     */
	@Override
	public String[] getUserCommand() {
		return ui.getUserCommand();
	}

}
