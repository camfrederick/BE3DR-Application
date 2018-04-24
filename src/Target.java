
public class Target {
	InputCoordinates input;
	OutputCoordinates output;
	float X;
	float Y;
	char lanePosition;
	String targetID;
	float metersFromBottom;
/*	
	public Target(float x, float y, float z, float r, float v, float vx, float vy, float vz){
		input = new InputCoordinates(x, y, z, r, v, vx, vy, vz);
		output = new OutputCoordinates(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		X = x;
		Y = y;
	}*/
	
	public Target(float x, float y, float v, String target) {
		input = new InputCoordinates(x, y, v);
		output = new OutputCoordinates(0, 0, 0);
		X = x;
		Y = y;
		lanePosition = 'Q';
		targetID = target;
		metersFromBottom = Float.valueOf(String.format("%.3f", (float) Math.sqrt(Math.pow(x,2) + Math.pow(y, 2))));
	}
	
	public InputCoordinates getInputCoordinates(){
		return input;
	}
	
	public OutputCoordinates getOutputCoordinates(){
		return output;
	}
	
	public float getX(){
		return X;
	}
	
	public float getY(){
		return Y;
	}
	
	public float getRange(){
		return metersFromBottom;
	}
	
	public String getID(){
		return targetID;
	}
	
	public char getLanePosition() {
		return lanePosition;
	}
	
	public void transform(){
		output = CoordTransforms.transform(input);
		X = output.getX();
		Y = output.getY();
		calculateLanePosition();
	}
	
	public void calculateLanePosition() {
		int numLanes = RadarVideo.getNumLanes();
		if(numLanes == 2)
			if(this.getX() < 195)
				lanePosition = 'A';
			else {
				if(this.getX() >= 195)
					lanePosition = 'B';
				else
					lanePosition = 'Z';
			}
		else {
			if(numLanes == 4) {
				if(this.getX() >= 367.5)
					lanePosition = 'D';
				else {
					if(this.getX() >= 245)
						lanePosition = 'C';
					else {
						if(this.getX() >= 122.5)
							lanePosition = 'B';
						else {
							if(this.getX() >= 0)
								lanePosition = 'A';
							else
								lanePosition = 'Z';
						}
					}
				}
			}
			else
				lanePosition = 'Q';
		}	
	}
}
