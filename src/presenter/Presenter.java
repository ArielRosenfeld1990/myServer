package presenter;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import server.MyServer;
import view.View;

public class Presenter implements Observer {
    View view;
    MyServer myServer;
    public Presenter(View v,MyServer server){
    	view=v;
    	myServer=server;
    }
	@Override
	public void update(Observable o, Object arg) {
		if (o==view){
			String[] input = view.getUserCommand();
			switch (input[0]){
			case "start":try {
					myServer.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					view.display(e);
				}
			break;
			case "exit":
				view.stop();  
				myServer.close();
			break;
			case "saveToXML":myServer.setXMLproperties(input);
			break;
			default:
				view.display("not a valid command");
			break;
			}
		}
      if (o==myServer){
    	  view.display(arg);
      }
	}
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}

}
