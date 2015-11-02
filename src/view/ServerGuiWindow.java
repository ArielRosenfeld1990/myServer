package view;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * <h1>ServerGuiWindow</h1>
 * ServerGuiWindow class is extending BasicWindow because this class is a 
 * type of a BasicWindow, implementing View interface
 * @author Ariel Rosenfeld and Ofir Calif
 *
 */


public class ServerGuiWindow extends BasicWindow implements View {
	String[] inputStrings;
	Button Start;
	Button Disconnect;
	Button Exit;
	Button SetProperties;
	Text ClientStatus;
	/**
	 * <h1>ServerGuiWindow constructor</h1>
	 * ServerGuiWindow constructor initiliazing the BasicWindow constructor
	 * @param title is the title for the window
	 * @param width is the width of the window
	 * @param height is the height of the window
	 */
	public ServerGuiWindow(String title, int width, int height) {
		super(title, width, height);
		
	}
	/**
	 * <h1>initWidgets</h1>
	 * initWidgets method is setting our shell and all our
	 * widgets in the gui window
	 */
	@Override
	void initWidgets() {
		shell=new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setLayout(new GridLayout(10, false));
		shell.setText("Server GUI Window");
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		
		InputStream imageStream;
		try { 
			imageStream = new BufferedInputStream(new FileInputStream("resources/dark_theme.jpg"));
			Image I= new Image(null, imageStream);
			shell.setBackgroundImage(I);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Start=new Button(shell, SWT.PUSH);
		Start.setText("Connect");
		Start.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, false, false, 7, 1));
		Start.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Start.setEnabled(false);
				inputStrings = new String[] { "start" };

				setChanged();
				notifyObservers();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ClientStatus=new Text(shell, SWT.MULTI | SWT.BORDER);
		ClientStatus.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,3,3));
		ClientStatus.setEnabled(false);
		
		SetProperties=new Button(shell,SWT.PUSH);
		SetProperties.setText("Change Server Properties");
		SetProperties.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,7, 1));
		SetProperties.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				SetProperties.setEnabled(false);
				PropertiesWindow();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		Disconnect=new Button(shell, SWT.PUSH);
		Disconnect.setText("Disconnect");
		Disconnect.setEnabled(false);
		Disconnect.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,7, 1));
		Disconnect.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Disconnect.setEnabled(false);
				inputStrings = new String[] { "disconnect" };
				setChanged();
				notifyObservers();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		Exit=new Button(shell, SWT.PUSH);
		Exit.setText("Exit");
		Exit.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false,7, 1));
		Exit.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				stop();

				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
     
		shell.addDisposeListener(new DisposeListener() {
		
		@Override
		public void widgetDisposed(DisposeEvent arg0) {
			inputStrings = new String[] { "exit" };
			setChanged();
			notifyObservers();
			
		}
	});
	}
	/**
	 * <h1>start</h1>
	 * start method is overriding the start method in the View interface,
	 * used for starting the interaction between the gui window and the client
	 */
	@Override
	public void start() {
		run();

	}
    /**
     * <h1>display</h1>
     * display method is overriding the display method in the View interface,
     * used for displaying the proper results in the proper widgets
     */
	@Override
	public void display(Object obj) {
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if (obj != null) {
					switch (obj.getClass().getSimpleName()) {
					case "String":
						String message = (String) obj;
						if (message.startsWith("Waiting for client connection")){
							ClientStatus.append('\n'+message);
							break;
						}
						if(message.startsWith("client connected")){
							ClientStatus.append('\n'+message);

							break;
						}
						if (message.startsWith("server address:")){
							ClientStatus.append(message);
							Disconnect.setEnabled(true);
							Start.setEnabled(false);
							SetProperties.setEnabled(false);
							break;
						}
						MessageBox mBox = new MessageBox(shell, SWT.OK);
						mBox.setMessage(message);
						mBox.open();
						if(message.equals("XML saved successfully")) {
							break;
						}
                     if (message.startsWith("Client disconnected")){
                    	 ClientStatus.setText(" ");
							Start.setEnabled(true);
							SetProperties.setEnabled(true);
                    	 break;
                     }

			      }
				}
				
			}});

	}
	@Override
	/**
	 * <h1>stop</h1>
	 * stop method is overriding the stop method in the View interface,
	 * used for stopping the interaction between the gui window and the client
	 */
	public void stop() {
		shell.dispose();

	}
    /**
     * <h1>getUserCommand</h1>
     * getUserCommand is overriding the getUserCommand in the View interface for getting
     * the command from the client
     */
	@Override
	public String[] getUserCommand() {
		return inputStrings;
	}
   /**
    * <h1>PropertiesWindow</h1>
    * PropertiesWindow method is used for configuring our PropertiesWindow,
    * setting the shell and the widgets in this window
    */
	void PropertiesWindow(){
	    Shell PropertiesShell=new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
	    PropertiesShell.setSize(200, 300);
	    
	    PropertiesShell.setLayout(new GridLayout(1, false));
	    PropertiesShell.setText("Server Properties Window");
	    
		Label PortNumberLabel=new Label(PropertiesShell, 1);
		PortNumberLabel.setText("Enter the port number");
		PortNumberLabel.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		Text PortNumberText=new Text(PropertiesShell,SWT.BORDER);
		PortNumberText.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		PortNumberText.setTextLimit(4);
		Label ClientsNumberLabel=new Label(PropertiesShell, 1);
		ClientsNumberLabel.setText("Enter the number of clients");
		ClientsNumberLabel.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		Text ClientsNumberText=new Text(PropertiesShell,SWT.BORDER);
		ClientsNumberText.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		ClientsNumberText.setTextLimit(3);
		Label timeoutLabel=new Label(PropertiesShell, 1);
		timeoutLabel.setText("Enter server timeout");
		timeoutLabel.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		Text timeoutText=new Text(PropertiesShell,SWT.BORDER);
		timeoutText.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		timeoutText.setTextLimit(5);
		Label searcherLabel=new Label(PropertiesShell, 1);
		searcherLabel.setText("Enter Searcher");
		searcherLabel.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		Text searcherText=new Text(PropertiesShell,SWT.BORDER);
		searcherText.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		searcherText.setTextLimit(3);
		Button setProperties=new Button(PropertiesShell, SWT.PUSH);
		setProperties.setText("set");
		setProperties.setLayoutData(new GridData(SWT.LEFT, SWT.None, false, false, 1, 2));
		setProperties.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (ClientsNumberText.getText()!=""&&PortNumberText.getText()!=""){
					inputStrings = new String[] { "saveToXML", ClientsNumberText.getText(), PortNumberText.getText(),timeoutText.getText(),searcherText.getText()};
					setChanged();
					notifyObservers();
					PropertiesShell.close();
				}
				else {
					MessageBox mPropertiesBox = new MessageBox(shell, SWT.OK);
					mPropertiesBox.setMessage("Must enter all the fields");
					mPropertiesBox.open();
					PropertiesShell.forceFocus();
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
				
			}
		});

		PropertiesShell.setVisible(true);
		PropertiesShell.addDisposeListener(new DisposeListener() {  
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				SetProperties.setEnabled(true);
				
			}
		});
  }
   


}
