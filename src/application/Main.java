package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;



public class Main extends Application {
//	public HBox addHBox() {
//	    HBox hbox = new HBox();
//	    hbox.setPadding(new Insets(15, 12, 15, 12));
//	    hbox.setSpacing(10);
//	    hbox.setStyle("-fx-background-color: #336699;");
//
//	    Button but = new Button();
//		Button but1 = new Button();
//		
//		but.setText("Hello");
//		but.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				// TODO Auto-generated method stub
//				System.out.println(12 <= 13 ? 45 : 89);
//				
//			}});
//		
//		// setting location of button
//		//but1.setTranslateX(100);
//		//but1.setTranslateY(0);
//		but1.setText("Ahihihi"); // name of button
//		
//		// action of button
//		but1.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				// TODO Auto-generated method stub
//				System.out.println("Ahihih");
//				
//			}});
//		
//	    
//	    hbox.getChildren().addAll(but1, but);
//
//	    return hbox;
//	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
			// store the root element so that the controllers can use it
			BorderPane rootElement = (BorderPane) loader.load();
			// create and style a scene
			Scene scene = new Scene(rootElement, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("JavaFX meets OpenCV");
			primaryStage.setScene(scene);
			// show the GUI
			primaryStage.show();
			
			// set the proper behavior on closing the application
			FXController controller = loader.getController();
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we)
				{
					controller.setClosed();
				}
			}));
			
			/*
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,500,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			HBox hbox = addHBox();
			root.setTop(hbox);
			
			
			
			primaryStage.show();
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		launch(args);
	}
}
