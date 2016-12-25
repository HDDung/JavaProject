package facial_recognition;


import core.TrainingPacket;
import org.opencv.core.Mat;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgproc.Imgproc;



public class Recognizer {
	public static final double RECOGNITION_THRESHOLD = 1500;
	private boolean State = false;
	private FaceRecognizer faceRecognizer;
	private TrainingPacket packet;
	private double[] confidence;
	private int[] labels;

	public Recognizer(){
		faceRecognizer = Face.createEigenFaceRecognizer(80, 2500);
		System.out.println("Recognizer created");
	}
	
	public void Training(TrainingPacket packet){
		if (State == false){
			this.packet = packet;
	        faceRecognizer.train(packet.getImages(), packet.getLabelsBuffer());
			confidence = new double[packet.getName().size()];
			labels = new int[packet.getName().size()];
			System.out.println("Training finish");
			State = true;
		}
	}

	public RecognizedFace Prediction(Mat testImage) {

		Mat temp = new Mat();
		Imgproc.cvtColor(testImage, temp, Imgproc.COLOR_BGR2GRAY);
    	Imgproc.resize(temp, temp, packet.getStandardImgSize());
		faceRecognizer.predict(temp, labels, confidence);
		if (confidence[0] < RECOGNITION_THRESHOLD && labels[0] != 0) {

			//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: " + Name.get(predictedLabel.intValue()));
			return new RecognizedFace(packet.getName().get(labels[0]), confidence[0]);
		}
		//System.out.println("Predicted label: " + predictedLabel.toString() + " Name: Unknown");

		return new RecognizedFace("Unknown", 0.0);

	}
	
	

    
}
