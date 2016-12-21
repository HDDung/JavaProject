package core;

import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

import facial_detection.Detector;
import facial_recognition.Recognizer;

public class Supervisor {
	private Detector dtor;
	private Recognizer recog;
	private DataHandler handler;
	private Vector<Mat> unknownFaces;
	
	public Supervisor(){
		this.dtor = new Detector();
		this.recog = new Recognizer();
		this.handler = new DataHandler();
		recog.Training(handler.getData("Face_Recog/B"));
	}
	public Mat Processframe(Mat frame){
		MatOfRect faces = dtor.detectAndDisplay(frame);
		
		Vector<String> name = PredictionFace(faces, frame);
				
		return dtor.DrawnFace(faces, frame, name);
	}
	
	private Vector<String> PredictionFace(MatOfRect faces, Mat frame){
		Vector<String> result = new Vector<String>();
		for (Rect face : faces.toArray()){
			result.addElement(new String(recog.Prediction(new Mat(frame, face ))));
		}
		return result;
	}
	
	public String IntegrityName(String name){
		String result = new String();
		if (handler.IsNameIn(name)){
			Vector<Integer> ListID = handler.ListID(name);
			Integer ID = new Integer(0);
			ID = ShowImg();
			if (!ID.equals(0)) {
				result = "Face_Recog/B/" +  ID + "-" + name + "-"+ handler.NumOfIn(ID).toString() + "-.bmp";
			} else {
				result = "Face_Recog/B/" +  handler.LatestLabel().toString() + "-" + name + "-1-.bmp";
			}
		} else {
			//index-name-1-.bmp
			result = "Face_Recog/B/" +  handler.LatestLabel().toString() + "-" + name + "-1-.bmp";
		}
		return result;
	}
	
	private Integer ShowImg(){
		
		return 0;
	}
	// can be change to static
	public Vector<Mat> UnknownFaces(Mat frame){
		unknownFaces = new Vector<Mat>();
		MatOfRect faces = dtor.detectAndDisplay(frame);
		Vector<String> name = PredictionFace(faces, frame);
		
		int count = 0;
		for (Rect face : faces.toArray()){
			if (name.get(count).equals("Unknown")){
				unknownFaces.addElement(new Mat(frame, face ));
			}
		}
		return this.unknownFaces;
	}
}
