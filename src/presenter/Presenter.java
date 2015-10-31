package presenter;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import server.MyServer;
import view.View;
/**
 * <h1>Presenter</h1>
 * @author Ariel Rosenfeld and Ofir Calif
 * our Presenter class is observing the View and Model layers and activing the proper
 * methods in those layers according to the change
 *
 */
public class Presenter implements Observer {
    private View view;
    private MyServer myServer;
    /**
     * <h1>Presenter constructor</h1>
     * 
     * our Presenter constructor is initiliazing his view and model data members
     *@param v is the View layer
     *@param server is the Model layer
     */
    public Presenter(View v,MyServer server){
    	view=v;
    	myServer=server;
    }
    /**
     * <h1>update</h1>
     * 
     *update method is being used when a change occurred in our View or Model observables
     *and reacts according to the change in the relevant layer
     *@param o is the observable object
     *@param arg is the object that transfered when a change occurred
     */
	@Override
	public void update(Observable o, Object arg) {
		if (o==view){
			String[] input = view.getUserCommand();
			switch (input[0]){
			case "start":try {
					myServer.start();
				} catch (IOException e) {
					
					view.display(e);
				}
			break;
			case "disconnect": 
				myServer.disconnect();
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
    /**
     * <h1>getView</h1>
     * 
     *getView is a getter for the view datamember
     *@return view is the returned view layer
     */
	public View getView() {
		return view;
	}
    /**
     * <h1>setView</h1>
     * 
     *setView is a setter for the view datamember
     *@param view is the data member view
     */
	public void setView(View view) {
		this.view = view;
	}

}
