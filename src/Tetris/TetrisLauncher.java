package Tetris;

import java.io.File;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *                                                                                                                  
 * This class runs the <code>ArcadeApp.java</code> program.              
 *                                                                                                                     
 * @author Timothy Xu <thx35333@uga.edu>                                                                  
 */
public class TetrisLauncher extends Application {

    Random rng = new Random();

    /**
     * Starts the program/GUI
     */
    @Override
    public void start(Stage stage) {
        final Menu menu1 = new Menu("File");
	    final Menu menu2 = new Menu("Options");
	    final Menu menu3 = new Menu("Help");
	    MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menu1, menu2, menu3);
		// creates the menu items inside of the menus
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(actionEvent -> Platform.exit());
	    // adds the new menu items to the menu and sets the instance menubar equal to the the local menubar
	    menu1.getItems().add(exitItem);
	    
	    Image image = new Image("file:mineBackground.jpg");
	    ImageView iv = new ImageView(image);
	    
	    StackPane stackpane = new StackPane();
		HBox hbox = new HBox(300);
		
		//button 1 and image for button 1
		Button button1 = new Button();
		button1.setOnAction((event) -> {							
			Tetris game1 = new Tetris();
			game1.startTetris();
		});		
		Image tetrisImage = new Image("file:tetris.jpg");
		ImageView tetrisView = new ImageView(tetrisImage);
		tetrisView.setFitHeight(160);
		tetrisView.setFitWidth(160);
		button1.setGraphic(tetrisView);
		
		//hbox to hold the two butons, stackpane to put the buttons over the background
		hbox.setAlignment(Pos.CENTER);
//		hbox.setPadding(new Insets(30));
		hbox.getChildren().addAll(button1);
		stackpane.getChildren().addAll(iv, hbox);
		
		// vbox to hold the menubar and stackpane
	    VBox vbox = new VBox();
	    vbox.getChildren().addAll(menuBar, stackpane);
	    
	    // creates loading screen
	    Image loadingGif = new Image("file:giphy.gif");
	    ImageView loading = new ImageView(loadingGif);
	    loading.setFitHeight(580);
		loading.setFitWidth(740);
	    StackPane stackpane2 = new StackPane();
	    stackpane2.getChildren().addAll(loading);
	    Scene loadingScreen = new Scene(stackpane2, 740, 580);
	    stage.setScene(loadingScreen);
	    
	    // sets scene equal to main menu
        Scene scene = new Scene(vbox, 740, 580);
        // when the user clicks on the loading screen, send to main menu
     	loadingScreen.setOnMouseClicked(event -> {
     		System.out.println(event);
     		stage.setScene(scene);
     	});
     		
        // sets menu bar equal to scene size
        menuBar.setMinWidth(scene.getWidth());
        // sets image equal to scene size
        iv.setFitWidth(scene.getWidth());
	    iv.setFitHeight(scene.getHeight());
	    stage.setResizable(false);
        stage.setTitle("cs1302-arcade!");    
        stage.sizeToScene();
        stage.show();
    } // start

    public static void main(String[] args) {
		try {
		    Application.launch(args);
		} catch (UnsupportedOperationException e) {
		    System.out.println(e);
		    System.err.println("If this is a DISPLAY problem, then your X server connection");
		    System.err.println("has likely timed out. This can generally be fixed by logging");
		    System.err.println("out and logging back in.");
		    System.exit(1);
		} // try
    } // main

} // ArcadeApp
