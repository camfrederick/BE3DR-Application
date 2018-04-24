public class InputCoordinates {
		public static float X;
		public static float Y;
		public static float Z;
		public static float R;
		public static float V;
		public static float Vx;
		public static float Vy;
		public static float Vz;
		
/*		public InputCoordinates(float x, float y, float z, float r, float v, float vx, float vy, float vz) {
			X = x;
			Y = y;
			Z = z;
			R = r;
			V = v;
			Vx = vx;
			Vy = vy;
			Vz = vz;
		}*/
		
		public InputCoordinates(float x, float y, float v){
			X = x;
			Y = y;
			V = v;
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