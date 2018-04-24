import java.math.*;

import sun.text.resources.cldr.de.FormatData_de_CH;

import java.io.*;

public class CoordTransforms {
	
	private static float Xs;
	private static float Zs;
	private static float Theta_hs;
	private static float Theta_vs;
	protected static float FOVh;
	protected static float FOVv;
	private static float numLanes;
	
	/*
	 * Xs = Sensor X offset from center
	 * Theta_hs = Sensor Horizontal Tilt Angle
	 * Zs = Sensor Height above Target
	 * Theta_vs = Sensor Vertical Tilt Angle
	 * Xt = Target X position
	 * Yt = Target Y position
	 * Vxt = Target X Velocity component
	 * Vyt = Target Y Velocity component
	 * FOVh = Camera Horizontal Field of View
	 * FOVv = Camera Vertical Field of View
	 * Ix = Infinity point for the horizontal axis
	 * Iz = Infinity point for the vertical axis
	 */
	public void initCoordTransforms(float xs, float zs, float theta_hs, float theta_vs, float fovh, float fovv, float lanes){
		// The following parameters cannot be deduced from data provided by the sensor and must be
		// specified as part of system setup
		Xs = xs;
		Zs = zs;
		Theta_hs = theta_hs;
		Theta_vs = theta_vs;
		FOVh = fovh;
		FOVv = fovv;
		numLanes = lanes;
    	System.out.println("Coordinate Transforms initialized as:\nXs = " + Xs + "\nZs = " + Zs + "\nTheta_hs = " + Theta_hs + "\nTheta_vs = " + Theta_vs + "\nFOVh = " + FOVh + "\nFOVv = " + FOVv + "\nLanes = " + numLanes + "\n\n");
	}
	
	public void initCoordTransforms(float xs, float zs, float theta_hs, float theta_vs, float lanes){
		// The following parameters cannot be deduced from data provided by the sensor and must be
		// specified as part of system setup
		Xs = xs;
		Zs = zs;
		Theta_hs = theta_hs;
		Theta_vs = theta_vs;
		numLanes = lanes;
    	System.out.println("Coordinate Transforms initialized as:\nXs = " + Xs + "\nZs = " + Zs + "\nTheta_hs = " + Theta_hs + "\nTheta_vs = " + Theta_vs + "\nLanes = " + numLanes + "\n\n");
	}
	
	public static OutputCoordinates transform(InputCoordinates input) {
		
//		float Ix = -2.0f * Theta_hs/FOVh;
//		float Iz = -2.0f * Theta_vs/FOVv;
		float Xvt = input.getX() - Xs;
		float Xvr = (float)(Xvt * Math.cos(Theta_hs) + input.getY() * Math.sin(Theta_hs));
		float Yvr = (float)(input.getY()  * Math.cos(Theta_hs) - Xvt * Math.sin(Theta_hs));
		float Zvr = -1 * Zs;
		float Xr = Xvr;
		float Yr = (float) (Yvr - Zvr * Math.sin(-Theta_vs));
		/*System.out.println("In roadway->video, Input X = " + input.getX() + ", Y = " + input.getY() + 
				", Xvt = " + Xvt + ", Xvr = " +Xvr + ", Yvr = " + Yvr + 
				"\n");*/
		
		float Zr = (float)(Zvr * Math.cos(-Theta_vs) - Yvr * Math.sin(-Theta_vs));
		//System.out.println("In roadway->video, Input X = " + input.getX() + ", Y = " + input.getY() + 
		//		", Z = " + input.getZ() + ", Xvt = " + Xvt + ", Xvr = " +Xvr + ", Yvr = " + Yvr + 
		//		", Zvr = " + Zvr + "\n");
		
/*		float x = 0;
		float z = 0;
		
		if(Yr != 0.0f){
			x = 2.0f * Xr/(Yr *FOVh);
			z = 2.0f * Zr/(Yr * FOVv);
		}
		else {
			x = 1.0f;
			z = 1.0f;
		}
		float y = 0.0f;
//		float r = input.getR();
		float Theta_Y = 0.0f;
		float Theta_Z = (float)Math.atan2(x, z); */
		
//		float VXvr = (float)(input.getVX() * Math.cos(-Theta_hs) + input.getVY() * Math.sin(-Theta_hs));
//		float VYvr = (float)(input.getVY() * Math.cos(-Theta_hs) + input.getVX() * Math.sin(-Theta_hs));
//		float VZvr = input.getVZ();
//		float VXr = VXvr;
//		float VYr = (float)(VYvr + Math.cos(-Theta_vs) - VZvr * Math.sin(-Theta_vs));
//		float VZr = (float)(VZvr + Math.cos(-Theta_vs) - VYvr * Math.sin(-Theta_vs));
		
//		float Vx = 2.0f * VXr/(Yr * FOVh);
//		float Vy = 0.0f;
//		float Vz = 2.0f * VZr/(Yr * FOVv);
//		float Theta_Vy = 0.0f;
//		float Theta_Vz = (float)Math.atan2(OutputCoordinates.Vx, OutputCoordinates.Vz);
//		float IX = Ix;
//		float IZ = Iz;

		/*OutputCoordinates output = new OutputCoordinates(x, y, z, r, Vx, Vy, Vz, V, IX, IZ, Theta_Vy, Theta_Vz, Theta_Y, Theta_Z);
		
		return output;*/
		float velocity = Math.abs(input.getV());
		float X = Xr * -1;
		float Y = Yr;
		float outputX = X + (FOVh / 2);
		float outputY = (Y - FOVv) * -1;
		if(numLanes == 2){
			outputX = (outputX * 390) / FOVh;
			outputY = (outputY * 750) / FOVv;
		}
		else {
			outputX = (outputX * 490) / FOVh;
			outputY = (outputY * 750) / FOVv;
		}
		
		return new OutputCoordinates(outputX, outputY, velocity);
	}
}
