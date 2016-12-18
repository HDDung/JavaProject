package facial_recognition;

import java.io.File;
import java.io.FilenameFilter;
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

import javafx.scene.Node;

public class Recognizer3 {
	private boolean State = false;
	private FaceRecognizer faceRecognizer;
	private List<Mat> images;
	private MatOfInt labelsBuffer; 
	private HashMap<Integer, String> Name = new HashMap<>();
	private Size StandardImgSize;
	public Recognizer3(int a){
		
		faceRecognizer = Face.createEigenFaceRecognizer(80, 2500);
		Training();
		System.out.println("Recognizer created");
	}
	
	//@SuppressWarnings({ "unchecked", "rawtypes" })
	public void Training(){
		if (State == false){
			String trainingDir = "Face_Recog/B";
			
	        //org.bytedeco.javacpp.opencv_core.Mat testImage = Recognizer2.MatCVtoMatJavaCpp(frame);
	        //opencv_imgcodecs.imwrite("Test.bmp", testImage);
	        File root = new File(trainingDir);

	        FilenameFilter imgFilter = new FilenameFilter() {
	            public boolean accept(File dir, String name) {
	                return name.endsWith(".jpg") || name.endsWith(".bmp") || name.endsWith(".png");
	            }
	        };

	        File[] imageFiles = root.listFiles(imgFilter);

			images = new ArrayList<Mat>(imageFiles.length);

	        labelsBuffer = new MatOfInt(new int[imageFiles.length]);
	        
	        int counter = 0;
	        
	        
	        for (File image : imageFiles) {
	            Mat img = Imgcodecs.imread(image.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
	            StandardImgSize = img.size();
	            Integer label = new Integer(Integer.parseInt(image.getName().split("\\-")[0]));
	            this.Name.put(label, image.getName().split("\\-")[1]);
	            images.add(img);

	            labelsBuffer.put(counter++, 0, label.intValue());

	            counter++;
	        }
	        faceRecognizer.train(images, labelsBuffer);
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
    	Imgproc.resize(temp, temp, StandardImgSize);
        //FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
		//System.out.println(temp.size());

        // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer()
    	
        Integer predictedLabel = new Integer(faceRecognizer.predict(temp));
        if (predictedLabel.intValue() != 0){
        	//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: " + Name.get(predictedLabel.intValue()));
    		return Name.get(predictedLabel.intValue());
        } 
    	//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: Unknown");

        return "Unknown";
        
	}
	
	

    
}
