package core;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class DataHandler {
	
	private List<Mat> images;
	private MatOfInt labelsBuffer; 
	private HashMap<Integer, Mat> ID_Face = new HashMap<>();
	private HashMap<Integer, Integer> ID_Number = new HashMap<>();
	private HashMap<Integer, String> Name = new HashMap<>();
	private Size StandardImgSize;
	private TrainingPacket trainingPacket;

	public static void updateData(String Name, Mat face, Size standardImgSize) {

		System.out.print("Name will be write: " + Name);
		if (!face.empty() && !Name.isEmpty()) {
			Imgproc.resize(face, face, standardImgSize);
			Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
			Imgcodecs.imwrite(Name, face);
		} else {
			System.err.println("written Face is empty");
		}
	}

	public void getData(String trainingDir) {
		// refresh container;
		ID_Face = new HashMap<>();
		ID_Number = new HashMap<>();
		Name = new HashMap<>();
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
        	// Get Img from directory
            Mat img = Imgcodecs.imread(image.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            StandardImgSize = img.size();
            //
            Integer label = new Integer(Integer.parseInt(image.getName().split("\\-")[0]));

            // Each ID (label) have a name
            this.Name.put(label, image.getName().split("\\-")[1]);
            // Each ID have a face represented;
			this.ID_Face.put(label, img);

			//Each ID have number of face
			if (ID_Number.containsKey(label)) {
				ID_Number.replace(label, new Integer(ID_Number.get(label).intValue() + 1));
			} else {
				ID_Number.put(label, new Integer(1));
            }


			images.add(img);

            labelsBuffer.put(counter++, 0, label.intValue());

            counter++;
        }
		trainingPacket = new TrainingPacket(images, labelsBuffer, StandardImgSize, Name);
	}

	public TrainingPacket getTrainingPacket() {
		return trainingPacket;
	}

	public boolean IsNameIn(String Name){
		for (int i = 1; i <= LatestLabel(); i++){
			if (Name.equals( this.Name.get(i))){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Checking the algorithm
	 * */
	@SuppressWarnings("unused")
	private void Check(){
		for (int i = 1; i <= ID_Number.size(); i++){
			System.out.println(ID_Number.get(i));
		}
	}

	public Mat GetFaceOf(Integer ID) {
		return ID_Face.get(ID);
	}
	/*
	 * Return the List of ID that have the same name;
	 * */
	public Vector<Integer> ListID(String name){
		Vector<Integer> result = new Vector<>();
		for (int i = 1; i <= Name.size(); i++) {
			if (Name.get(i).equals(name)){
				result.addElement(i);
			}
		}
		return result;
	}
	
	/*
	 * Return the highest index in database
	 * 
	 * */
	public Integer LatestLabel(){
		return new Integer(Name.size());
	}

	/*
	 * Return the number of faces of a ID */
	public Integer NumOfIn(Integer ID){
		return ID_Number.get(ID);
	}

	public Size getStandardImgSize() {
		return StandardImgSize;
	}
}
