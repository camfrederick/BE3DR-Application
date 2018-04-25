import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import javafx.scene.control.Button;

public class Application {

	private static ArrayList<File> videoFiles = new ArrayList<File>();
	private static ArrayList<File> radarOutput = new ArrayList<File>();
	
	public static String VID_FOLDER = "C:/Users/Cam/workspace/BeeperGUI/Video";
	public static String OUTPUT_FOLDER = "C:/Users/Cam/workspace/BeeperGUI/RadarOutput";

	public static int numButtons = findFolders(VID_FOLDER, OUTPUT_FOLDER);
	
	public static FileSelection mainMenu;
	public static VideoInput applicationWindow;
	public static boolean opening = true;
	
	public static void main(String[] args)
	{
		mainMenu = new FileSelection(findFolders(VID_FOLDER, OUTPUT_FOLDER), videoFiles, radarOutput);
		//applicationWindow = new RadarVideo(videoFiles.get(FileSelection.index).toURI().toString(), radarOutput.get(FileSelection.index).toString(), 4);
	}
	
	public static int findFolders(String vid, String out) {
		File v = new File(vid);
		File o = new File(out);
		File[] video = v.listFiles();
		File[] output = o.listFiles();
		
		videoFiles.clear();
		radarOutput.clear();
		
		for(int i = 0; i < video.length; i++) {
			String s = video[i].toString();
			s = s.substring(s.length() - 3);
			if(s.equals("mp4")){
				videoFiles.add(video[i]);
			}
		}
		
		for(int i = 0; i < output.length; i++) {
			String t = output[i].toString();
			t = t.substring(t.length() - 3);
			if(t.equals("txt")){
				radarOutput.add(output[i]);
			}
		}
		
		if(videoFiles.size() != radarOutput.size()){
			System.err.println("Missing a file");
			new ChangeInputPage();
		}
		
		int numButtons = radarOutput.size();
		
		if(videoFiles.size() < numButtons)
			numButtons = videoFiles.size();
		
		return numButtons;
		
	}
	
	public static int getNumButtons() {
		return numButtons;
	}
	
	public static void setVideoInput(String newVideoFolder) {
		VID_FOLDER = newVideoFolder;
	}
	
	public static void setRadarInput(String newRadarFolder) {
		OUTPUT_FOLDER = newRadarFolder;
	}
	
}
