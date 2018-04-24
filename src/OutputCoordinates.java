
public class OutputCoordinates {
	public static float X;
	public static float Y;
	public static float Z;
	public static float R;
	public static float V;
	public static float Theta_Y;
	public static float Theta_Z;
	public static float Vx;
	public static float Vy;
	public static float Vz;
	public static float Theta_Vy;
	public static float Theta_Vz;
	public static float Ix;
	public static float Iz;

	
	public OutputCoordinates(float x, float y, float v) {
		X = x;
		Y = y;
		V = v;
	}
	public OutputCoordinates(){
		
	}
	
	public float getX(){
		return X;
	}
	
	public float getY(){
		return Y;
	}
/*	
	public float getZ(){
		return Z;
	}
	
	public float getR(){
		return R;
	}*/
	
	public float getV(){
		return V;
	}
/*	
	public float getVX(){
		return Vx;
	}
	
	public float getVY(){
		return Vy;
	}
	
	public float getVZ(){
		return Vz;
	}*/
}
