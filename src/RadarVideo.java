import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Insets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


public class RadarVideo{

	private static final long serialVersionUID = 1L;
	private static final Border red = new Border(new BorderStroke(Color.RED,
        BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2)));
    private static final Border blue = new Border(new BorderStroke(Color.BLUE,
        BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2)));
    private static final Border green = new Border(new BorderStroke(Color.GREEN,
            BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(2)));
    private JFXPanel panel;
    public static String vid_url;
    protected static int laneNum;
    private ArrayList<Target> prevTargets = new ArrayList<Target>();
    private ArrayList<Target> currentTargets = new ArrayList<Target>();
    private ArrayList<String> prevLabels = new ArrayList<String>();
    private ArrayList<String> metaLabels = new ArrayList<String>();
    int currentFrame = 1;
    private final JFrame jFrame = new JFrame();
    float fps = 1;

	public RadarVideo(String vid, String output, int lanes, float framesPerSecond)
	{   	
		fps = framesPerSecond;
		panel = new JFXPanel();
		laneNum = lanes;
		vid_url = vid;
		Platform.runLater(new Runnable()
		{
			public void run()
			{
				BorderPane grid = new BorderPane();
				grid.setPadding(new Insets(16));
				
				BorderPane buttons = new BorderPane();
				buttons.setPadding(new Insets(16));
				
				Pane animation = new Pane();
				grid.setCenter(animation);
			    // Put canvas in the center of the window
			    Canvas roadway = new Canvas();
			    animation.getChildren().add(roadway);
			    // Bind the width/height property to the wrapper Pane
			    roadway.widthProperty().bind(animation.widthProperty());
			    roadway.heightProperty().bind(animation.heightProperty());
			    // redraw when resized
			    if(laneNum == 2){ 
			    	roadway.widthProperty().addListener(event -> drawTwoRoadway(roadway));
			    	roadway.heightProperty().addListener(event -> drawTwoRoadway(roadway));
			    
			    	
			    }
			    else{
			    	roadway.widthProperty().addListener(event -> drawFourRoadway(roadway));
			    	roadway.heightProperty().addListener(event -> drawFourRoadway(roadway));
			    	
			    }
			    	
			    animation.setPadding(new Insets(8));
			    animation.setMaxWidth(590);
			    animation.setMinWidth(590);
			    animation.setMaxHeight(800);
			    animation.setMinHeight(800);
			    animation.setBorder(green);
				
				BorderPane video = new BorderPane();
				video.setPadding(new Insets(8));
				video.setBorder(red);
				
				Pane radarData = new Pane();
				radarData.setPadding(new Insets(30));
				radarData.setBorder(blue);
				radarData.setMaxHeight(800);
				radarData.setMinHeight(800);
				Canvas metaData = new Canvas();
			    radarData.getChildren().add(metaData);
			    // Bind the width/height property to the wrapper Pane
			    metaData.widthProperty().bind(radarData.widthProperty());
			    metaData.heightProperty().bind(radarData.heightProperty());
			    
			    metaData.widthProperty().addListener(event -> updateMetaData(metaData, metaLabels));
		    	metaData.heightProperty().addListener(event -> updateMetaData(metaData, metaLabels));
				
				updateTargets(output, 0, roadway, metaData);
				
				final Media clip = new Media(vid_url);
				final MediaPlayer player = new MediaPlayer(clip);
				final MediaView viewer = new MediaView(player);
				viewer.setFitHeight(800);
				viewer.setFitWidth(800);
				
				final Button newFile = new Button("Select New File");
				final Button fromBeginning = new Button("Restart");
				final Button pausePlay = new Button("Play");
				
				newFile.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event)
					{
						Application.mainMenu.getFrame().setVisible(true);
						jFrame.dispose();
						return;
					}
				});
				
				fromBeginning.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event)
					{
						viewer.getMediaPlayer().pause();
						viewer.getMediaPlayer().seek(Duration.ZERO);
						viewer.getMediaPlayer().pause();
						
						pausePlay.setText("Play");
						currentFrame = 0;
						updateTargets(output, 0, roadway, metaData);
					}
				});
				pausePlay.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event)
					{
						if(pausePlay.getText().equals("Play")){
							viewer.getMediaPlayer().play();
							pausePlay.setText("Pause");
							new Thread(()-> {
								animateRoadway(output, roadway, viewer, metaData);
								//update();
     							
								
							}).start();
							
						}
						else{
							viewer.getMediaPlayer().pause();
							pausePlay.setText("Play");
						}
					}
				});
				

/*				viewer.getMediaPlayer().statusProperty().addListener(new ChangeListener<Status>() {
					@Override
					public void changed(ObservableValue<? extends Status> arg0, Status arg1, Status arg2) {
						// TODO Auto-generated method stub
						if(arg2.equals(Status.PLAYING))
							animateRoadway(output, roadway, v, viewer);
					}
				});
				
				viewer.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {

					@Override
					public void changed(ObservableValue<? extends Duration> arg0, Duration arg1, Duration arg2) {
						// TODO Auto-generated method stub
						if(arg2.equals(Duration.ZERO)){
							System.out.println("resetting animation");
							currentFrame = 0;
							initializeTargets(output, 0, roadway);
						}
					}
					
				});*/
				
				setMediaEventHandlers(viewer);
				
				
								
				video.setCenter(viewer);	
								
				radarData.setMinWidth(470);
				radarData.setMaxWidth(470);
				
				buttons.setLeft(pausePlay);
				pausePlay.setTranslateX(815);
				pausePlay.setPrefWidth(90);
				buttons.setCenter(fromBeginning);
				fromBeginning.setPrefWidth(90);
				fromBeginning.setTranslateX(-7);
				
				
				grid.setLeft(video);
				grid.setBottom(buttons);
				grid.setRight(radarData);
				grid.setTop(newFile);
				newFile.setTranslateY(-10);
				
				Scene aScene = new Scene(grid);
				panel.setScene(aScene);
			}	
		});
		jFrame.add(panel);
		jFrame.setSize(2000, 1000);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}

	public void hide(){
		jFrame.setVisible(false);
	}
	
	private void setMediaEventHandlers(final MediaView view) {
		final MediaPlayer player = view.getMediaPlayer();
		
		System.out.println("Initial: " + player.getStatus());
		player.statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
			@Override
			public void changed(ObservableValue<? extends MediaPlayer.Status> observable, MediaPlayer.Status oldStatus, MediaPlayer.Status curStatus) {
				System.out.println("Current: " + curStatus);
			}
		});

		if (player.getError() != null) {
			System.out.println("Initial Error: " + player.getError());
		}

		player.setOnError(new Runnable() {
			@Override public void run() {
				System.out.println("Current Error: " + player.getError());
			}
		});
	}
	
	private void drawTwoRoadway(Canvas r) {
		int width = (int) r.getWidth();
	    int height = (int) r.getHeight();
	    GraphicsContext gc = r.getGraphicsContext2D();
	    gc.clearRect(0, 0, width, height);
	    gc.setFill(Color.GRAY);
        gc.fillRect(100, 5, width - 200, height - 30);
	    
        int midLine = width / 2;
        
	    gc.setStroke(Color.GOLD);
	    gc.strokeLine(midLine + 5, 5, midLine + 5, height - 25);
	    gc.strokeLine(midLine - 5, 5, midLine - 5, height - 25);
	        
	    gc.setFill(Color.WHITE);
	    gc.setStroke(Color.WHITE);
        gc.setFont(new Font(null, 20));
        gc.setLineWidth(2);
	    gc.strokeText("A", (midLine - 100), 725);
        gc.strokeText("B", (midLine + 100), 725);
	    
        int roadwayHeight = height - 50;
        int roadIncrements = roadwayHeight / 5;
        int bottomRoad = roadwayHeight + 25;
        
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font(null, 17));
        gc.strokeText((int)CoordTransforms.FOVv  + "m", width - 90, 30);
        gc.strokeText((int)(CoordTransforms.FOVv * 4 / 5) + "m", width - 90, bottomRoad - (4 * roadIncrements) + 5);
        gc.strokeText((int)(CoordTransforms.FOVv * 3 / 5) + "m", width - 90, bottomRoad - (3 * roadIncrements) + 5);
        gc.strokeText((int)(CoordTransforms.FOVv * 2 / 5) + "m", width - 90, bottomRoad - (2 * roadIncrements) + 5);
        gc.strokeText((int)(CoordTransforms.FOVv * 1 / 5) + "m", width - 90, bottomRoad - roadIncrements + 5);
        gc.strokeText("0m", width - 90, bottomRoad + 5);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.strokeLine(width - 110, 25, width - 100, 25);
        gc.strokeLine(width - 110, bottomRoad - (4 * roadIncrements), width - 100, bottomRoad - (4 * roadIncrements));
        gc.strokeLine(width - 110, bottomRoad - (3 * roadIncrements), width - 100, bottomRoad - (3 * roadIncrements));
        gc.strokeLine(width - 110, bottomRoad - (2 * roadIncrements), width - 100, bottomRoad - (2 * roadIncrements));
        gc.strokeLine(width - 110, bottomRoad - roadIncrements, width - 100, bottomRoad - roadIncrements);
        gc.strokeLine(width - 110, bottomRoad, width - 100, bottomRoad);
        
        gc.setLineWidth(2);
        
        drawTargets(gc);
	}

	private void drawFourRoadway(Canvas r) {
        int width = (int) r.getWidth();
        int height = (int) r.getHeight();
        GraphicsContext gc = r.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setFill(Color.GRAY);
        gc.fillRect(50, 5, width - 100, height - 30);
        
        gc.setStroke(Color.GOLD);
        int midLane = (width / 2);
        int firstLane = (midLane + 45) / 2;
        int thirdLane = midLane + (midLane - firstLane);
        
        gc.strokeLine(midLane + 5, 5, midLane + 5, height - 25);
        gc.strokeLine(midLane - 5, 5, midLane - 5, height - 25);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(firstLane, 25, firstLane, 40);
        gc.strokeLine(firstLane, 70, firstLane, 85);
        gc.strokeLine(firstLane, 115, firstLane, 130);
        gc.strokeLine(firstLane, 160, firstLane, 175);
        gc.strokeLine(firstLane, 205, firstLane, 220);
        gc.strokeLine(firstLane, 250, firstLane, 265);
        gc.strokeLine(firstLane, 295, firstLane, 310);
        gc.strokeLine(firstLane, 340, firstLane, 355);
        gc.strokeLine(firstLane, 385, firstLane, 400);
        gc.strokeLine(firstLane, 430, firstLane, 445);
        gc.strokeLine(firstLane, 475, firstLane, 490);
        gc.strokeLine(firstLane, 520, firstLane, 535);
        gc.strokeLine(firstLane, 565, firstLane, 580);
        gc.strokeLine(firstLane, 610, firstLane, 625);
        gc.strokeLine(firstLane, 655, firstLane, 670);
        gc.strokeLine(firstLane, 700, firstLane, 715);
        gc.strokeLine(firstLane, 745, firstLane, 760);
        gc.strokeLine(thirdLane, 25, thirdLane, 40);
        gc.strokeLine(thirdLane, 70, thirdLane, 85);
        gc.strokeLine(thirdLane, 115, thirdLane, 130);
        gc.strokeLine(thirdLane, 160, thirdLane, 175);
        gc.strokeLine(thirdLane, 205, thirdLane, 220);
        gc.strokeLine(thirdLane, 250, thirdLane, 265);
        gc.strokeLine(thirdLane, 295, thirdLane, 310);
        gc.strokeLine(thirdLane, 340, thirdLane, 355);
        gc.strokeLine(thirdLane, 385, thirdLane, 400);
        gc.strokeLine(thirdLane, 430, thirdLane, 445);
        gc.strokeLine(thirdLane, 475, thirdLane, 490);
        gc.strokeLine(thirdLane, 520, thirdLane, 535);
        gc.strokeLine(thirdLane, 565, thirdLane, 580);
        gc.strokeLine(thirdLane, 610, thirdLane, 625);
        gc.strokeLine(thirdLane, 655, thirdLane, 670);
        gc.strokeLine(thirdLane, 700, thirdLane, 715);
        gc.strokeLine(thirdLane, 745, thirdLane, 760);  
        
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(null, 20));
        gc.setLineWidth(2);
        gc.strokeText("A", firstLane - 60, 725);
        gc.strokeText("B", firstLane + 60, 725);
        gc.strokeText("C", thirdLane - 60, 725);
        gc.strokeText("D", thirdLane + 60, 725);
        
        int roadwayHeight = height - 50;
        int roadIncrements = roadwayHeight / 5;
        int bottomRoad = roadwayHeight + 25;
        
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font(null, 17));
        gc.strokeText((int)CoordTransforms.FOVv + "m", width - 45, 30);
        gc.strokeText((int)(CoordTransforms.FOVv * 4 / 5) + "m", width - 45, bottomRoad - (4 * roadIncrements) + 5);
        gc.strokeText((int)(CoordTransforms.FOVv * 3 / 5) + "m", width - 45, bottomRoad - (3 * roadIncrements) + 5);
        gc.strokeText((int)(CoordTransforms.FOVv * 2 / 5) + "m", width - 45, bottomRoad - (2 * roadIncrements) + 5);
        gc.strokeText((int)(CoordTransforms.FOVv * 1 / 5) + "m", width - 45, bottomRoad - roadIncrements + 5);
        gc.strokeText("0m", width - 45, bottomRoad + 5);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.strokeLine(width - 60, bottomRoad - (4 * roadIncrements), width - 50, bottomRoad - (4 * roadIncrements));
        gc.strokeLine(width - 60, bottomRoad - (3 * roadIncrements), width - 50, bottomRoad - (3 * roadIncrements));
        gc.strokeLine(width - 60, bottomRoad - (2 * roadIncrements), width - 50, bottomRoad - (2 * roadIncrements));
        gc.strokeLine(width - 60, bottomRoad - roadIncrements, width - 50, bottomRoad - roadIncrements);
        gc.strokeLine(width - 60, bottomRoad, width - 50, bottomRoad);
        gc.setLineWidth(2);
        
        drawTargets(gc);
	}
	
	private void updateMetaData(Canvas metaData, ArrayList<String> labels) {
		int width = (int) metaData.getWidth();
        int height = (int) metaData.getHeight();
		GraphicsContext gc = metaData.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        
        for(int i = 0; i < labels.size(); i++){
        	gc.strokeText(labels.get(i), 15, (i*30) + 35);
        }
	}
	
	private void drawTargets(GraphicsContext gc) {
        
        int zeroX = 0;
        int zeroY = 25;
        if(laneNum == 2){
        	zeroX = 100;
        }
        else{
        	zeroX = 50;
        }
        	
        gc.setStroke(Color.CHARTREUSE);
        
        for(int i = 0; i < currentTargets.size(); i++){
        	gc.setLineWidth(7);
        	float x = currentTargets.get(i).getX() + zeroX;
        	float y = currentTargets.get(i).getY() + zeroY;
        	gc.strokeRect(x - 20, y - 20, 40, 40); 
        	gc.setLineWidth(2);
        	int id = Integer.valueOf(currentTargets.get(i).getID());
        	if(id < 10)
        		gc.strokeText(currentTargets.get(i).getID(), x-6, y + 6);
        	if(id < 100 && id >= 10)
        		gc.strokeText(currentTargets.get(i).getID(), x-12, y + 6);
        	if(id < 1000 && id >= 100)
        		gc.strokeText(currentTargets.get(i).getID(), x-16.8, y + 6);
        	
        }
        
        gc.setLineWidth(2);
        
        
    }
	
	private boolean updateTargets(String outputFile, int frame, Canvas road, Canvas metaData) {
		File f = new File(outputFile);
		prevLabels.clear();
		prevLabels.addAll(metaLabels);
		metaLabels.clear();
		prevTargets.clear();
		prevTargets.addAll(currentTargets);
		currentTargets.clear();
		int countFrames = 0;
		int everyTenFrames = 0;
		
		Scanner in;
		try {
			in = new Scanner(f);
			String currentLine;
			while(countFrames != frame && in.hasNextLine())
			{
				currentLine = in.nextLine();
				if(!in.hasNext())
					return false;
				else{
					if(in.next().equals("-")){
						everyTenFrames++;
						everyTenFrames = everyTenFrames % 1;
						if(everyTenFrames == 0)
							countFrames++;
					}
				}
			}
				
			do{
				//String first = in.next();
				if(in.hasNext()){
					String target = in.next();
					if(target.equals("-"))
						break;
					else{
						float y = in.nextFloat();
						float x = in.nextFloat();
						//float r = in.nextFloat();
						float vel = in.nextFloat();
						//float vx = in.nextFloat();
						//float vy = in.nextFloat();
						String speed = Float.toString(vel) + "m/s";
						//String range = Float.toString(r) + "m";
						//Target t = new Target(x, y, 0, r, vel, vx, vy, 0);
						Target t = new Target(x, y, vel, target);
						t.transform();
					
						currentTargets.add(t);
						String l = "Target " + target + ": " + speed + "; Lane: " + t.getLanePosition() + "; Range: " + t.getRange() + "m \n";
						metaLabels.add(l);
					}	
				}
				else{
					currentTargets.addAll(prevTargets);
					metaLabels.addAll(prevLabels);
				}
			}while(in.hasNextLine());
			
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(laneNum == 2)
			drawTwoRoadway(road);
		else
			drawFourRoadway(road);
		
		updateMetaData(metaData, metaLabels);
		
		jFrame.repaint();
				
		return true;
	}
	
	private void animateRoadway(String outputFile, Canvas road, MediaView viewer, Canvas metaData) {
		while(viewer.getMediaPlayer().getStatus().equals(Status.PLAYING) || viewer.getMediaPlayer().getStatus().equals(Status.READY)){
			float fps = this.fps;
			long time = System.currentTimeMillis();
			updateTargets(outputFile, currentFrame, road, metaData);
			
			currentFrame++;
			float delay = ( 1000 / fps) - (System.currentTimeMillis() - time);
			if(delay > 0) {
				try{
					Thread.sleep((long)delay);
				}catch(Exception e){};
			}
		}
			
	}
	
	public static int getNumLanes() {
		return laneNum;
	}

}