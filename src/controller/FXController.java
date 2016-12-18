package controller;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opencv.core.Mat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Utils;


/**
 * The controller for our application, where the application logic is
 * implemented. It handles the button for starting/stopping the camera and the
 * acquired video stream.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author <a href="http://max-z.de">Maximilian Zuleger</a> (minor fixes)
 * @version 2.0 (2016-09-17)
 * @since 1.0 (2013-10-20)
 *
 */
public class FXController  
{
	
	// a flag to change the button behavior
	private boolean IsLoadCameraScreen = false;
	// Child controller
	private CameraController camera_controller;
	// Stage of child
	private Stage camera_stage;
	private boolean holder = true;
	
	// the FXML button
		@FXML
		private Button button, but_cap, but_detect, NextBut;
		// the FXML image view
		@FXML
		private ImageView currentFrame;
		@FXML
		private TextField Name;
		
	
	/**
	 * The action triggered by pushing the button on the GUI
	 *
	 * @param event
	 *            the push button event
	 * @throws IOException 
	 */
	
	private void LoadCameraScreen() throws IOException{
			try {
				System.out.println("Open new screen");
				FXMLLoader loader_camera = new FXMLLoader(getClass().getResource("/fxml/Camera_Screen.fxml"));
				Parent root1 = (Parent) loader_camera.load();
				camera_stage = new Stage();
				camera_stage.setTitle("Camera");
				camera_stage.setScene(new Scene(root1, 600, 400));
				camera_stage.show();
				camera_controller = new CameraController();
				camera_controller = loader_camera.getController();
				
				
				//set the proper behavior on closing the camera_screen
				camera_stage.setOnCloseRequest((new EventHandler<WindowEvent>() {
					public void handle(WindowEvent we)
					{
						camera_controller.setClosed();
						IsLoadCameraScreen = false;
						button.setText("Start Camera");
						but_detect.setText("Start detection");
						
					}
				}));
				
			} catch (Exception e){
				System.err.println("Cannot open Camera Screen");
			}
		
		
	}
	
	
	@FXML
	protected void startCamera(ActionEvent event) 
	{
		// if Camera Screen not load
		if (!IsLoadCameraScreen){
			try {
				this.LoadCameraScreen();
				this.camera_controller.startCamera();
				this.button.setText("Stop Camera");
				this.IsLoadCameraScreen = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else // Change title of button
			if (!this.camera_controller.IsCameraRun()){
			this.camera_controller.startCamera();
			this.button.setText("Start Camera");
			
		} else {
			this.camera_controller.startCamera();
			this.button.setText("Stop Camera");
		}
			
			
	}
	
	@FXML 
	protected void CaptureFace(ActionEvent event) throws InterruptedException{
		System.out.println("Capture call");
		Name.clear();
		final Vector<Mat> unknownFaces = this.camera_controller.UnknownFaces();
		System.out.println(unknownFaces.size());
		if (!unknownFaces.isEmpty()){
			Runnable unknown = new Runnable() {
				public void run() {
					for (Mat face : unknownFaces) {
						//Mat test = Imgcodecs.imread("Test.bmp");
						Image imageToShow = Utils.mat2Image(face);
						CameraController.updateImageView(currentFrame, imageToShow);
						while (holder){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								Thread.currentThread().interrupt();
							}
							
						}
						holder = true;
					}
				}
			};
			
			ExecutorService run = Executors.newSingleThreadExecutor();
			run.submit(unknown);
			holder = true;
			run.shutdownNow();
			
		} else {
			System.out.println("No unknown face");
		}
	}
	
	@FXML
	protected void MoveNextImg(ActionEvent event){
		System.out.println(Name.getText());
		holder = false;
		Name.clear();
	}
	
	@FXML 
	protected void StartDetection(ActionEvent event){
		if (IsLoadCameraScreen) {
			if (!this.camera_controller.IsActivateDetector()) {
				this.camera_controller.ActivateDetector();
				this.but_detect.setText("Stop detection");
			} else {
				this.camera_controller.DeactivateDetector();
				this.but_detect.setText("Start detection");
			} 
		} else {
			System.err.println("Camera didn't open");
		}
	}
	
	
	/**
	 * On application close, stop the acquisition from the camera
	 */
	public void setClosed()
	{
		if (IsLoadCameraScreen) {
			this.camera_controller.setClosed();
			this.camera_stage.close();
		}

	}


	
	
	
	
}