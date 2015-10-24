package view;

import java.util.Observable;
import java.util.Observer;

public class MyServerCliView extends Observable implements Observer, View {
    ServerCLI ui;
	@Override
	public void start() {
		ui = new ServerCLI(System.in, System.out);
		ui.addObserver(this);
		ui.start();  

	}

	@Override
	public void stop() {
		ui.close();

	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == ui) {
			setChanged();
			notifyObservers();
		}
 
	}

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

	@Override
	public String[] getUserCommand() {
		return ui.getUserCommand();
	}

}
