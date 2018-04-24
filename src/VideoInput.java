import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
import javafx.scene.control.TextField;
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

public class VideoInput{

	private final JFrame jFrame = new JFrame();
	
	private static final long serialVersionUID = 1L;
	
	private JFXPanel panel;
	
	public VideoInput(String vid, String output){
		panel = new JFXPanel();
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				//Creating a GridPane container
				GridPane grid = new GridPane();
				grid.setPadding(new Insets(10, 10, 10, 10));
				grid.setVgap(5);
				grid.setHgap(5);
				//Defining the Name text field
				final TextField xs = new TextField();
				xs.setPromptText("Xs: ");
				xs.setPrefColumnCount(10);
				xs.getText();
				GridPane.setConstraints(xs, 0, 0);
				grid.getChildren().add(xs);
				//Defining the Last Name text field
				final TextField zs = new TextField();
				zs.setPromptText("Zs: ");
				GridPane.setConstraints(zs, 0, 1);
				grid.getChildren().add(zs);
				//Defining the Comment text field
				final TextField thetahs = new TextField();
				thetahs.setPrefColumnCount(15);
				thetahs.setPromptText("Theta_hs: ");
				GridPane.setConstraints(thetahs, 1, 0);
				grid.getChildren().add(thetahs);
				final TextField thetavs = new TextField();
				thetavs.setPrefColumnCount(15);
				thetavs.setPromptText("Theta_vs: ");
				GridPane.setConstraints(thetavs, 1, 1);
				grid.getChildren().add(thetavs);
				final TextField fovh = new TextField();
				fovh.setPrefColumnCount(15);
				fovh.setPromptText("FOVh: ");
				GridPane.setConstraints(fovh, 2, 0);
				grid.getChildren().add(fovh);
				final TextField fovv = new TextField();
				fovv.setPrefColumnCount(15);
				fovv.setPromptText("FOVv: ");
				GridPane.setConstraints(fovv, 2, 1);
				grid.getChildren().add(fovv);
				final TextField lanes = new TextField();
				lanes.setPrefColumnCount(15);
				lanes.setPromptText("Number of Lanes: ");
				GridPane.setConstraints(lanes, 0, 2);
				grid.getChildren().add(lanes);
				final TextField fps = new TextField();
				fps.setPrefColumnCount(15);
				fps.setPromptText("Frames per Second: ");
				GridPane.setConstraints(fps, 1, 2);
				grid.getChildren().add(fps);
				//Defining the Submit fovv
				Button submit = new Button("Submit");
				GridPane.setConstraints(submit, 2, 4);
				submit.setOnAction(new EventHandler<ActionEvent>() {
					boolean badInput = true;
					@Override
					public void handle(ActionEvent event)
					{
						CoordTransforms in = new CoordTransforms();
						boolean cont = false;
						int numLanes = 0;
						try{
							in.initCoordTransforms(Float.parseFloat(xs.getText()), Float.parseFloat(zs.getText()), Float.parseFloat(thetahs.getText()), Float.parseFloat(thetavs.getText()), Float.parseFloat(fovh.getText()), Float.parseFloat(fovv.getText()), Float.parseFloat(lanes.getText()));
							//in.initCoordTransforms(Float.parseFloat(xs.getText()), Float.parseFloat(zs.getText()), Float.parseFloat(thetahs.getText()), Float.parseFloat(thetavs.getText()), Float.parseFloat(lanes.getText()));
							numLanes = Integer.parseInt(lanes.getText());
							if(numLanes != 2 && numLanes != 4){
								Text t = new Text("Incorrect number of lanes. Please try again");
								t.setFill(Color.RED);
								GridPane.setConstraints(t, 2, 5);
								grid.getChildren().add(t);
								paint(grid, panel);
								this.badInput = false;
							}
							else
								cont = true;
						} catch (NumberFormatException e) {
							if(badInput){
								Text t = new Text("Incorrect input. Please try again");
								t.setFill(Color.RED);
								GridPane.setConstraints(t, 2, 5);
								grid.getChildren().add(t);
								paint(grid, panel);
								this.badInput = false;
							}
							
						}
						if(cont){
							new RadarVideo(vid, output, numLanes, Float.parseFloat(fps.getText()));
							buttonPushed();
						}
					}
				});
				
				grid.getChildren().add(submit);
				
				//Defining the Clear button
				Button clear = new Button("Clear All");
				GridPane.setConstraints(clear, 2, 5);
				clear.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event)
					{
						xs.clear();
						zs.clear();
						thetahs.clear();
						thetavs.clear();
						fovh.clear();
						fovh.clear();
						lanes.clear();
						fps.clear();
					}
				});
				grid.getChildren().add(clear);
				
				final Button newFile = new Button("Select New File");
				GridPane.setConstraints(newFile, 2, 6);
				newFile.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event)
					{
						Application.mainMenu.getFrame().setVisible(true);
						jFrame.dispose();
						return;
					}
				});
				grid.getChildren().add(newFile);
				
				
				Scene aScene = new Scene(grid);
				panel.setScene(aScene);
			}
		});
		jFrame.add(panel);
		jFrame.setSize(1000,500);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}
	
	public void paint(GridPane g, JFXPanel p) {
		
		jFrame.add(p);
		jFrame.setSize(1000, 500);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		
	}
	
	public void buttonPushed() {
		jFrame.dispose();
	}
	
}
