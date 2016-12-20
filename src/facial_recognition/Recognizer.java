package facial_recognition;

import java.io.File;
import java.io.FilenameFilter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import core.DataHandler;
import core.TrainingPacket;



public class Recognizer {
	private boolean State = false;
	private FaceRecognizer faceRecognizer;
	private DataHandler handler = new DataHandler();
	private TrainingPacket packet;
	public Recognizer(){
		faceRecognizer = Face.createEigenFaceRecognizer(80, 2500);
		Training();
		System.out.println("Recognizer created");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void Training(){
		if (State == false){
			packet = handler.getData("Face_Recog/B");
			
	        faceRecognizer.train(packet.getImages(), packet.getLabelsBuffer());
	        System.out.println("Training finish");
	        State = true;
		}
	}
	public String Prediction(Mat testImage){
		if (testImage == null){
	        testImage = Imgcodecs.imread("Face_Recog/face27.bmp", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		}
		Mat temp = new Mat();
		Imgproc.cvtColor(testImage, temp, Imgproc.COLOR_BGR2GRAY);
    	Imgproc.resize(temp, temp, packet.getStandardImgSize());
      
    	
        Integer predictedLabel = new Integer(faceRecognizer.predict(temp));
        if (predictedLabel.intValue() != 0){
        	//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: " + Name.get(predictedLabel.intValue()));
    		return packet.getName().get(predictedLabel.intValue());
        } 
    	//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: Unknown");

        return "Unknown";
        
	}
	
	

    
}
