package core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.AbstractMap.SimpleEntry;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

public class DataHandler {
	
	private List<Mat> images;
	private MatOfInt labelsBuffer; 
	private HashMap<Integer, Mat> ID_Face = new HashMap<>();
	private HashMap<Integer, Integer> ID_Number = new HashMap<>();
	private HashMap<Integer, String> Name = new HashMap<>();
	private Size StandardImgSize;

	public TrainingPacket getData(String trainingDir){
		
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
            if (ID_Number.containsKey(label)){
            	ID_Number.replace(label, new Integer(ID_Number.get(label).intValue() + 1));
            } else {
            	ID_Number.put(label, new Integer(1));
            }
            
            
            images.add(img);

            labelsBuffer.put(counter++, 0, label.intValue());

            counter++;
        }
		return new TrainingPacket(images, labelsBuffer, StandardImgSize, Name);
	}
	
	public static void updateData(Vector<String> Names, Vector<Mat> faces){
		for (int i = 0; i < faces.size(); i++){
			Imgcodecs.imwrite(Names.get(i), faces.get(i));
		}
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
	/*
	 * Return the List of ID that have the same name;
	 * */
	public Vector<Integer> ListID(String name){
		Vector<Integer> result = new Vector<>();
		for (int i = 0; i < Name.size(); i++){
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
}
