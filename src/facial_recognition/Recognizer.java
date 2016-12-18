package facial_recognition;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.Face;


public class Recognizer {
	
	private File Data = new File("Face_Recog");
	private Mat Y = new Mat();
	private int Threshold=6000;
	Size StandardImgSize = new Size(); 
	Mat eigenVectors = new Mat();
    Mat mean = new Mat();
    
    
    public void Training(){
		
		// Get img 
		
		Vector<Mat> faces = new Vector();
		
		File[] imageFiles = GetData();
		
        for (File image : imageFiles) {
            faces.addElement(Imgcodecs.imread(image.getAbsolutePath()));
        }
        
        int i = 0;
        StandardImgSize = faces.get(0).size();
        for (Mat face : faces){
        	Mat temp = new Mat();
        	Imgproc.resize(face, temp, StandardImgSize);
        	faces.set(i++, temp);
		}
        Mat List_32FC1 = new Mat();
        List_32FC1 = asRowMatrix(faces, CvType.CV_32FC1, 1, 0);
        
        
        
        
        Core.PCACompute(List_32FC1, mean, eigenVectors);
        
        for (int y = 0; y < faces.size(); y++){
			Mat t = List_32FC1.row(y).clone();
			Core.subtract(t, mean.reshape(1,1),t);
			Core.gemm(t,eigenVectors, 1.0, new Mat(), 0.0, t, Core.GEMM_2_T);
			Y.push_back(t);
		}
	}
	
	public void Prediction(){
		double mindist = 0;
		int minclas = 0;
		Mat Test_Img_p = null;
		mindist = Threshold;
		Mat Test_Img = Imgcodecs.imread("Face_Recog/face10.bmp");
		Imgcodecs.imwrite("Test.bmp", Test_Img);

		Imgproc.resize(Test_Img, Test_Img, StandardImgSize);

		Imgproc.cvtColor(Test_Img, Test_Img, Imgproc.COLOR_BGR2GRAY);
		System.out.println(mean.empty());
		Test_Img.reshape(1, 1).convertTo(Test_Img, CvType.CV_32FC1, 1, 0);
		Core.subtract(Test_Img, mean.reshape(1,1),Test_Img_p);
		Core.gemm(Test_Img_p,eigenVectors, 1.0, new Mat(), 0.0, Test_Img_p, Core.GEMM_2_T);
		//check all face that is in database (from 1 to Num_PC - # meaningful eigenfaces)
		for (int i=0; i < Y.rows(); i++){
			double dist = Core.norm(Y.row(i), Test_Img_p, Core.NORM_L2);

			if ((dist < mindist) && (dist < Threshold)){
				mindist = dist;
				minclas = i; // The i_th img that matched mostY
			}
		}
		
		Mat r = Y.row(minclas);
		Core.gemm(r,eigenVectors, 1.0, new Mat(), 0.0, r);
		Core.add(r, mean.reshape(1,1),r);
		
		String Name = "Prediction: ";
		if (mindist <= 3700.0){
			Name = Name + "Know";
		} else {
			Name = Name + "Unknow";
		}
		System.out.println(Name);
	}
	
	private Mat asRowMatrix(Vector<Mat> src, int rtype, double alpha, double beta) {
	    // Number of samples:
	    int n = src.size();
	    // Return empty matrix if no matrices given:
	    if(n == 0)
	        return new Mat();
	    // dimensionality of (reshaped) samples
	    Integer d = new Integer((int)src.get(0).total());
	    // Create resulting data matrix:
	    Mat data = new Mat(n, d, rtype);
	    // Now copy data:
	    for(Integer i = 0; i < n; i++) {
	        //
	        if(src.get(i).empty()) {
	            String error_message = new String("Image number" + i.toString() +" was empty, please check your input data.");
	            //Core.CV_Error(-5, error_message);
	            System.err.println(error_message);
	        }
	        // Make sure data can be reshaped, throw a meaningful exception if not!
	        if(src.get(i).total() != d) {
	        	
	            String error_message = new String("Wrong number of elements in matrix #"
	            		+ i.toString()+  "! Expected " + d.toString() +" was "+ new Long(src.get(i).total()).toString() );
	            System.err.println(error_message);
	            //CV_Error(-5, error_message);
	        }
	        // Get a hold of the current row:
	        Mat xi = data.row(i);
	        // Make reshape happy by cloning for non-continuous matrices:
	        if(src.get(i).isContinuous()) {
	            src.get(i).reshape(1, 1).convertTo(xi, rtype, alpha, beta);
	        } else {
	            src.get(i).clone().reshape(1, 1).convertTo(xi, rtype, alpha, beta);
	        }
	    }
	    return data;
	}
	
	
	private File[] GetData(){
		
		// to get only img file
		FilenameFilter imgFilter = new FilenameFilter() { // 
			@Override 
			/*override accept in the interface FilenameFilter
			 * and make sure that the parameter name has only img type file
			 * */
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".bmp") || name.endsWith(".png");
            }
        };
		
		File[] img = Data.listFiles(imgFilter);
		return img;
	}
	
}
