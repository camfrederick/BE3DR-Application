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
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	
	public static int index;
	
	public FileSelection(int num, ArrayList<File> videoFiles, ArrayList<File> radarOutput){
		
		panel = new JFXPanel();
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				VBox v = new VBox();
				for(int i = 0; i < num; i++){
					String s = videoFiles.get(i).toString();
					ScrollPane sp = new ScrollPane();
					int index = s.lastIndexOf('\\');
					s = s.substring(index + 1, s.length() -4);
					final Button button = new Button(s);
					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event)
						{
							FileSelection.index = -1;
							for(int j = 0; j < buttons.size(); j++){
								if(event.getSource().equals(buttons.get(j)))
									FileSelection.index = j;
							}
							//new VideoInput(videoFiles.get(index).toURI().toString(), radarOutput.get(index).toString());
							Application.main(null);
							//new RadarVideo(videoFiles.get(index).toURI().toString(), radarOutput.get(index).toString(), 4);
							buttonPushed();
						}
					});
					v.getChildren().addAll(button, sp);
					VBox.setVgrow(sp, Priority.ALWAYS);
					buttons.add(button);
				}
				
				Scene aScene = new Scene(v);
				panel.setScene(aScene);
			}	
		});
		jFrame.add(panel);
		jFrame.setSize(2000, 1000);
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
	
}
