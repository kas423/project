
package edu.dhbw.andar;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.java.ar.util.GraphicsUtil;



public abstract class ARObject {
	/**
	 * Is this object visible? -> is the marker belonging to this object visible?
	 */
	private boolean visible = false;
	private String name;
	private String patternName;
	private double markerWidth;
	private double[] center;
	//this object must be locked while altering the glMatrix
	private float[] glMatrix = new float[16];
	public  static float[] glCameraMatrix = new float[16];
	private FloatBuffer glMatrixBuffer;
	public static FloatBuffer glCameraMatrixBuffer;
	
	//this object must be locked while altering the transMat
	private double[] transMat = new double[16];//[3][4] array
	private int id;
	private boolean initialized = false;
	
	/**
	 * Create a new AR object.
	 * @param name the name of the the object, an arbitrary string
	 * @param patternName the file name of the pattern(the file must reside in the res/raw folder)
	 * @param markerWidth
	 * @param markerCenter
	 */
	public ARObject(String name, String patternName, double markerWidth, double[] markerCenter) {
		this.name = name;
		this.patternName = patternName;
		this.markerWidth = markerWidth;
		if(markerCenter.length == 2) {
			this.center = markerCenter;
		} else {
			this.center = new double[]{0,0};
		}
		glMatrixBuffer = GraphicsUtil.makeFloatBuffer(glMatrix);		
	}
	
	
	
	
	public double getMarkerWidth() {
		return markerWidth;
	}




	public double[] getCenter() {
		return center;
	}




	public int getId() {
		return id;
	}




	public  void setId(int id) {
		this.id = id;
	}

	



	public String getPatternName() {
		return patternName;
	}


	/**
	 * 
	 * @return Is this object visible? -> is the marker belonging to this object visible?
	 */
	public boolean isVisible() {
		return visible;
	}


	/**
	 * Get the current translation matrix.
	 * @return
	 */
	public synchronized double[] getTransMatrix() {
		return transMat;
	}
	
	/**
	 * Do OpenGL stuff.
	 * Everything draw here will be drawn directly onto the marker.
	 * 
	 * @param gl
	 */
	public synchronized void draw(GL10 gl) {
		if(!initialized) {
			init(gl);
			initialized = true;
		}
		if(glCameraMatrixBuffer != null) {
			glMatrixBuffer.put(glMatrix);
			glMatrixBuffer.position(0);
			
			//argDrawMode3D
			gl.glMatrixMode(GL10.GL_MODELVIEW);
		    gl.glLoadIdentity();
		    //argDraw3dCamera
		    gl.glMatrixMode(GL10.GL_PROJECTION);
		    gl.glLoadMatrixf( glCameraMatrixBuffer );
		    
		    gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadMatrixf(glMatrixBuffer);
		}
	}
	
	public abstract void init(GL10 gl); 
	
}