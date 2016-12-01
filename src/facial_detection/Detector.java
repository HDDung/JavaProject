package facial_detection;

/*
import java.awt.List;
import java.util.Vector;
import org.opencv.core.Core;*/
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

public class Detector {
	
	private String face_cascade_name = "HaarCascade/haarcascade_frontalface_alt_test.xml";
	private String eyes_cascade_name = "HaarCascade/haarcascade_eye_tree_eyeglasses.xml";
	private CascadeClassifier face_cascade = new CascadeClassifier(); //(Detector.class.getResource("HaarCascade/haarcascade_frontalface_alt.xml").getPath());;
//	private CascadeClassifier eyes_cascade;
	private int absoluteFaceSize = 0;
	
	public Detector(){
		try 
		{
			this.face_cascade.load( face_cascade_name );
//			eyes_cascade.load( eyes_cascade_name );
		} catch (CvException e){
			System.err.print("Error loading face cascade");
		}
	}
	
	public Mat detectAndDisplay(Mat frame){
		Mat grayFrame = new Mat();
		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		// compute minimum face size (20% of the frame height, in our case)
		if (this.absoluteFaceSize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}
		
		MatOfRect faces = new MatOfRect();
		
		// detect faces
		this.face_cascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
		
		
//		face_cascade.detectMultiScale( frame, faces, 1.05, 9, 0, new Size(30,30), new Size(70,70));
	    System.out.println(faces.toArray().length);
	    return DrawnFace(faces, frame);
		
	}
	
	private Mat DrawnFace(MatOfRect ListFaces, Mat frame){
		/*int thickness = 1;
		int fontFace = Core.FONT_HERSHEY_TRIPLEX; // font text;
		double fontScale = 0.5;
		int baseline;*/
		
		for (Rect face : ListFaces.toArray()){
//			Point center = new Point( face.x + face.width/2, face.y + face.height/2 );
			Imgproc.rectangle(frame, new Point(face.x, face.y), new Point(face.x + face.width, face.y + face.height), new Scalar(0, 255, 0));
		}
		
		return frame;
		
	}
	
}
