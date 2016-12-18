package controller;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

public class Controller extends StackPane {
    private HashMap<String, Node> screens = new HashMap<>();
    
    public void addScene(String name, Node node){
    	screens.put(name, node);
    }
    
    public void loadScene(String source){
    	try {
    		System.out.println("Open new screen");
    		FXMLLoader loader_camera = new FXMLLoader(getClass().getResource(source));
    		Parent root1 = (Parent) loader_camera.load();
    		Stage stage = new Stage();
    		stage.setTitle("Camera");
    		stage.setScene(new Scene(root1));
    		stage.show();
    		} catch (Exception e){
    			System.err.println("Cannot open Camera Screen");
    		}
    }
}	
