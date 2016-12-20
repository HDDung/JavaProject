package utils;

import org.opencv.core.Mat;

public class NameFaces {
	private Mat Faces;
	private boolean Check;
	private String Name;
	/**
	 * @return the faces
	 */
	public Mat getFaces() {
		return Faces;
	}
	/**
	 * @param faces the faces to set
	 */
	public void setFaces(Mat faces) {
		Faces = faces;
	}
	/**
	 * @return the check
	 */
	public boolean isCheck() {
		return Check;
	}
	/**
	 * @param check the check to set
	 */
	public void setCheck(boolean check) {
		Check = check;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
}
