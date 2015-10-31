package view;
/**
 * <h1>View</h1>
 * our View interface represents the view layer in our MVP structure, its responsible 
 * for viewing the information it got from his Observer - the Presenter
 * 
 * @author Ariel Rosenfeld and Ofir Calif
 * 
 * 
 * 
 *
 */
public interface View {
/**
 * <h1>start</h1>
 * start method is starting the interaction between the client and the view layer
 */
 void start();
 /**
  * <h1>display</h1>
  * display methods is responsible for displaying the results given from the Presenter 
  * to the screen
  * @param obj is the object that we want to display
  */
 void display(Object obj);
 /**
  * <h1>stop</h1>
  * stop method stops the interaction between the client and the view layer
  */
 void stop();
 /**
  * <h1>getUserCommand</h1>
  * getUserCommand method is getting a command from the client and inserting it
  * into a String array in order to transfer it to the relevant Observer
  * @return String[] is the insereted command from the client
  */
 String[] getUserCommand();
}
   