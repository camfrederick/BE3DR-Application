import javax.swing.JFrame;
import java.io.File;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

public class ChangeInputPage {

	protected final JFrame jFrame = new JFrame();
	
	private JFXPanel panel;
	
	public ChangeInputPage() {
		panel = new JFXPanel();
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				GridPane grid = new GridPane();
				grid.setPadding(new Insets(20));
				grid.setVgap(15);
				grid.setHgap(15);
				
				final TextField newRadar = new TextField();
				newRadar.setText(Application.OUTPUT_FOLDER);
				newRadar.setPrefColumnCount(15);
				newRadar.getText();
				GridPane.setConstraints(newRadar, 1, 2);
				grid.getChildren().add(newRadar);
				
				final TextField newVideo = new TextField();
				newVideo.setText(Application.VID_FOLDER);
				newVideo.setPrefColumnCount(15);
				
				GridPane.setConstraints(newVideo, 1, 3);
				grid.getChildren().add(newVideo);
				
				Button browseRadar = new Button("Browse Radar");
				GridPane.setConstraints(browseRadar, 2, 2);
				browseRadar.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event){
						DirectoryChooser directoryChooser = new DirectoryChooser();
						File selectedDirectory = directoryChooser.showDialog(null);
						if(selectedDirectory == null){
							newRadar.setText("No Directory selected");
						}else{
							newRadar.setText(selectedDirectory.getAbsolutePath());
						}
					}
				});
				grid.getChildren().add(browseRadar);
				
				Button browseVideo = new Button("Browse Video");
				GridPane.setConstraints(browseVideo, 2, 3);
				browseVideo.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event){
						DirectoryChooser directoryChooser = new DirectoryChooser();
						File selectedDirectory = directoryChooser.showDialog(null);
						if(selectedDirectory == null){
							newVideo.setText("No Directory selected");
						}else{
							newVideo.setText(selectedDirectory.getAbsolutePath());
						}
					}
				});
				grid.getChildren().add(browseVideo);
				
				Button submit = new Button("Submit");
				GridPane.setConstraints(submit, 2, 5);
				submit.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event){
						Application.setRadarInput(newRadar.getText());
						Application.setVideoInput(newVideo.getText());
						Application.main(null);
						buttonPushed();
					}
				});
				grid.getChildren().add(submit);
				
				Scene aScene = new Scene(grid);
				panel.setScene(aScene);
			}
		});
	
		jFrame.add(panel);
		jFrame.setSize(500, 300);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);

	}
	
	public void buttonPushed() {
		jFrame.dispose();
	}
}