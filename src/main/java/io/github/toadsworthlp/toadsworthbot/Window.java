package io.github.toadsworthlp.toadsworthbot;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Window {
	
	public static Stage stage;
	static Pane pnMain = new Pane();
	static final String windowTitle = "ToadsworthBot Control Panel";
	
	public static TextArea logArea = new TextArea();

	public Window(Stage stage){
		Window.stage = stage;
		
		//Set information like window title and window size 
		stage.setTitle(windowTitle);
		stage.setScene(new Scene(pnMain,351,300));
	}
	
	public static void showControls(){
		//Adding Controls
		TextField txtToken = new TextField();
		txtToken.setText("Token hier einsetzen");
		txtToken.setMinWidth(300);
		txtToken.setMaxWidth(300);
		Add(txtToken);
		
		TextField txtConfigURL = new TextField();
		txtConfigURL.setText("Link zu toadsworthbot.cfg");
		txtConfigURL.setLayoutY(35);
		txtConfigURL.setMinWidth(300);
		txtConfigURL.setMaxWidth(300);
		Add(txtConfigURL);
		
		Button btnLogin = new Button();
		btnLogin.setText("Start");
		btnLogin.setOnAction((event) -> btnLogin_Click(btnLogin, txtToken, txtConfigURL));
		btnLogin.setLayoutX(300);
		btnLogin.setMinHeight(65);
		Add(btnLogin);
		
		logArea.setMaxSize(350, 300);
		logArea.setLayoutY(70);
		logArea.setEditable(false);
		Add(logArea);
		
		//If the autoconnect arguments are valid, simulate a click on the start button
		if(ToadsworthBot.autoconnectInfo != null){
			if(ToadsworthBot.autoconnectInfo[0] != null && ToadsworthBot.autoconnectInfo[1] != null){
				txtToken.setText(ToadsworthBot.autoconnectInfo[0]);
				txtConfigURL.setText(ToadsworthBot.autoconnectInfo[1]);
				btnLogin_Click(btnLogin, txtToken, txtConfigURL);
			}
		}
		
		stage.show();
	}
	
	public static void btnLogin_Click(Button loginButton, TextField tokenField, TextField configField){
		Commander.configURL = configField.getText();		//Set config file URL
		ToadsworthBot.createAndLogin(tokenField.getText());		//Log the bot in using the given token
		Commander.LogInfo("ToadsworthBot gestartet.");		//Print a message into the control panel that it works
		
		//Disable all the controls - we don't need them anymore
		loginButton.setDisable(true);
		tokenField.setDisable(true);
		configField.setDisable(true);
	}
	
	//Convencience function
	public static void Add(Control control){
		pnMain.getChildren().add(control);
	}

}
