package core;

import org.opencv.core.Mat;

import facial_detection.Detector;
import facial_recognition.Recognizer;

public class Supervisor {
	private Detector dtor;
	private Recognizer recog;
	private DataHandler handler;
	public Supervisor(){
		this.dtor = new Detector();
		this.recog = new Recognizer();
		this.handler = new DataHandler();
	}
	public Mat Processframe(Mat frame){
		
		return new Mat();
	}
}
