package facial_detection;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;

import facial_recognition.Recognizer;

import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

public class Detector {
	
	private String face_cascade_name = "HaarCascade/haarcascade_frontalface_alt_test.xml";
	private CascadeClassifier face_cascade = new CascadeClassifier(); 


	public Detector(){
		try 
		{
			this.face_cascade.load( face_cascade_name );
			System.out.println("Creating finish");
		} catch (CvException e){
			System.err.print("Error loading face cascade");
		}
		//rec3.Prediction();
		
		
		
		
	}
	
	public MatOfRect detectAndDisplay(Mat frame){
		
		/*
		 * follow the tutorial from opencv-java-tutorials
		 * 
		 * */
		Mat grayFrame = new Mat();
		int absoluteFaceSize = 0;
		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		// compute minimum face size (20% of the frame height, in our case)
		if (absoluteFaceSize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				absoluteFaceSize = Math.round(height * 0.2f);
			}
		}
		
		MatOfRect faces = new MatOfRect();
		
		// detect faces
		this.face_cascade.detectMultiScale(grayFrame, faces, 1.1, 2,
				0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(absoluteFaceSize, absoluteFaceSize), new Size());
		
	    return faces;
		
	}
	
	public Mat DrawnFace(MatOfRect ListFaces, Mat frame, Vector<String> name){
			
		int count = 0;
		for (Rect face : ListFaces.toArray()){
			
			
			String text = name.elementAt(count++);
			
			Imgproc.rectangle(frame, new Point(face.x, face.y), new Point(face.x + face.width, face.y + face.height),
								new Scalar(0, 255, 0));
			
			Imgproc.putText(frame, text, new Point(face.x, face.y), Core.FONT_HERSHEY_TRIPLEX, 0.5,
					new Scalar(0, 255, 0), 1);
			
		}
			
		
		return frame;
		
	}
	
}
