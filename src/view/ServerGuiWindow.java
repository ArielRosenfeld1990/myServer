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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



public class ServerGuiWindow extends BasicWindow implements View {
	String[] inputStrings;
	Button Start;
	Button Exit;
	Button SetProperties;
	Text ClientStatus;
	public ServerGuiWindow(String title, int width, int height) {
		super(title, width, height);
		
	}
	@Override
	void initWidgets() {
		shell=new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setLayout(new GridLayout(10, false));
		shell.setText("Server GUI Window");
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		InputStream imageStream;
		try { 
			imageStream = new BufferedInputStream(new FileInputStream("lib/dark_theme.jpg"));
			Image I= new Image(null, imageStream);
			shell.setBackgroundImage(I);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Start=new Button(shell, SWT.PUSH);
		Start.setText("Connect");
        Start.setEnabled(false);

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
		SetProperties.setText("Set Server Properties");
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
	@Override
	public void start() {
		run();

	}

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
							ClientStatus.append(message);
							break;
						}
						if(message.startsWith("client connected")){
							ClientStatus.append(message);
							break;
						}
						if (message.startsWith("server address:")){
							ClientStatus.append(message);
							break;
						}
						MessageBox mBox = new MessageBox(shell, SWT.OK);
						mBox.setMessage(message);
						mBox.open();
						if(message.equals("XML saved successfully")) {
							Start.setEnabled(true);
							break;
						}


			      }
				}
				
			}});

	}
	@Override
	public void stop() {
		shell.dispose();

	}

	@Override
	public String[] getUserCommand() {
		return inputStrings;
	}
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
		Label IPaddressLabel=new Label(PropertiesShell, 1);
		IPaddressLabel.setText("Enter the server IP address");
		IPaddressLabel.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		Text IPaddressText=new Text(PropertiesShell,SWT.BORDER);
		IPaddressText.setLayoutData(new GridData(SWT.FILL, SWT.None, false, false, 1, 1));
		IPaddressText.setTextLimit(15);
		Button setProperties=new Button(PropertiesShell, SWT.PUSH);
		setProperties.setText("set");
		setProperties.setLayoutData(new GridData(SWT.LEFT, SWT.None, false, false, 1, 2));
		setProperties.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (ClientsNumberText.getText()!=""&&IPaddressText.getText()!=""&&PortNumberText.getText()!=""){
					inputStrings = new String[] { "saveToXML", ClientsNumberText.getText(), IPaddressText.getText(), PortNumberText.getText(),};
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
				// TODO Auto-generated method stub
				
			}
		});
		Button setDefaultProperties=new Button(PropertiesShell, SWT.PUSH);
		
		setDefaultProperties.setText("Default Properties");
		setDefaultProperties.setLayoutData(new GridData(SWT.LEFT, SWT.None, false, false, 1, 2));
		setDefaultProperties.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				SetProperties.setEnabled(true);
				inputStrings = new String[] { "saveToXML", "10", "127.0.0.1","5400",};
				setChanged();
				notifyObservers();
				PropertiesShell.close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
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
