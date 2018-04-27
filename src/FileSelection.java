import javafx.scene.paint.Color;
import javafx.geometry.Insets;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class FileSelection{

	private static final long serialVersionUID = 1L;
	
	private static ArrayList<Button> buttons = new ArrayList<Button>();	
	
	protected final JFrame jFrame = new JFrame();
	
	private JFXPanel panel;
	
	static ArrayList<File> videoInput;
	static ArrayList<File> radarInput;
	
	public FileSelection(int num, ArrayList<File> videoFiles, ArrayList<File> radarOutput){
		
		videoInput = videoFiles;
		radarInput = radarOutput;
		panel = new JFXPanel();
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				BorderPane pane = new BorderPane();
				GridPane samples = new GridPane();
				samples.setPadding(new Insets(14));
				samples.setHgap(10);
				samples.setVgap(20);
				
				for(int i = 0; i < num; i++){
					String s = videoInput.get(i).toString();
					int index = s.lastIndexOf('\\');
					s = s.substring(index + 1, s.length() -4);
					final Button button = new Button(s);
					button.setPrefWidth(225);
					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event)
						{
							int sample = -1;
							for(int j = 0; j < buttons.size(); j++){
								if(event.getSource().equals(buttons.get(j)))
									sample = j;
							}
							//new VideoInput(videoFiles.get(index).toURI().toString(), radarOutput.get(index).toString());
							//Application.main(null);
							//new RadarVideo(videoFiles.get(index).toURI().toString(), radarOutput.get(index).toString(), 4);
							new VideoInput(videoInput.get(sample).toURI().toString(), radarInput.get(sample).toString());
							buttonPushed();
						}
					});
					samples.add(button, i % 2, i / 2);
					
					buttons.add(button);
				}
				ScrollPane scrollPane = new ScrollPane(samples);
				
				
				MenuBar menuBar = new MenuBar();
				 
		        // --- Menu File
		        Menu changeInput = new Menu("Edit");
		        MenuItem changeInputFolders = new MenuItem("Change Input Folders");
		        changeInputFolders.setOnAction(new EventHandler<ActionEvent>() {
		            public void handle(ActionEvent t) {
		                new ChangeInputPage();
		                buttonPushed();
		            }
		        }); 
		        
		        
		        
		        changeInput.getItems().addAll(changeInputFolders);
		 
		        menuBar.getMenus().addAll(changeInput);
		 
		        pane.setTop(menuBar);
		        pane.setCenter(scrollPane);
		        
		        Scene aScene = new Scene(pane);
				panel.setScene(aScene);
			}	
		});
		jFrame.add(panel);
		jFrame.setSize(520, 800);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}
	
	public JFrame getFrame() {
		return jFrame;
	}
	
	public void buttonPushed() {
		jFrame.setVisible(false);
	}
	
	public static void setVideoInput(ArrayList<File> newVideoFolder) {
		videoInput = newVideoFolder;
	}
	
	public static void setRadarInput(ArrayList<File> newRadarFolder) {
		radarInput = newRadarFolder;
	}
}
