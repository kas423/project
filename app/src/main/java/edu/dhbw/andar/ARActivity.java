
package edu.dhbw.andar;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Debug;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.java.ar.exceptions.ARRuntimeException;
import com.java.ar.util.IO;
import com.java.arobjviewer.graphics.LightingRenderer;



public abstract class ARActivity extends Activity implements Callback, UncaughtExceptionHandler{
	private GLSurfaceView glSurfaceView;
	private Camera camera;
	private ARObjectRenderer renderer;
	private Resources res;
	private CameraPreviewHandler cameraHandler;
	private boolean mPausing = false;
	private ARToolkit artoolkit;
	private CameraStatus camStatus = new CameraStatus();
	private boolean surfaceCreated = false;
	private SurfaceHolder mSurfaceHolder = null;
	private Preview previewSurface;
	private boolean startPreviewRightAway;
	
	public ARActivity() {
		startPreviewRightAway = true;
	}
	
	public ARActivity(boolean startPreviewRightAway) {
		this.startPreviewRightAway = startPreviewRightAway;
	}

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.currentThread().setUncaughtExceptionHandler(this);
        res = getResources();
        
        artoolkit = new ARToolkit(res, getFilesDir());
        setFullscreen();
        disableScreenTurnOff();
        //orientation is set via the manifest
        
        try {
			IO.transferFilesToPrivateFS(getFilesDir(),res);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ARRuntimeException(e.getMessage());
		}
		FrameLayout frame = new FrameLayout(this);
		previewSurface = new Preview(this);
				
        glSurfaceView = new GLSurfaceView(this);
		renderer = new ARObjectRenderer(res, artoolkit, this);
		cameraHandler = new CameraPreviewHandler(glSurfaceView, renderer, res, artoolkit, camStatus);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glSurfaceView.getHolder().addCallback(this);
        
        frame.addView(glSurfaceView);
        frame.addView(previewSurface);
        
        setContentView(frame);
    }
    
    
    /**
     * Set a renderer that draws non AR stuff. Optional, may be set to null or omited.
     * and setups lighting stuff.
     * @param customRenderer
     */
    public void setNonARRenderer(LightingRenderer customRenderer) {
		renderer.setNonARRenderer(customRenderer);
	}

    /**
     * Avoid that the screen get's turned off by the system.
     */
	public void disableScreenTurnOff() {
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
    			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
	/**
	 * Set's the orientation to landscape, as this is needed by AndAR.
	 */
    public void setOrientation()  {
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    
    /**
     * Maximize the application.
     */
    public void setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
   
    public void setNoTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    } 
    
    @Override
    protected void onPause() {
    	mPausing = true;
        this.glSurfaceView.onPause();
        super.onPause();
        finish();
        if(cameraHandler != null)
        	cameraHandler.stopThreads();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();    	
    	System.runFinalization();
    	if(Config.DEBUG)
    		Debug.stopMethodTracing();
    }
    
    

    @Override
    protected void onResume() {
    	mPausing = false;
    	glSurfaceView.onResume();
        super.onResume();
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    /**
     * Open the camera.
     */
    private void openCamera()  {
    	if (camera == null) {
	    	//camera = Camera.open();
    		camera = CameraHolder.instance().open();
    		   		    		
    		
    		
    		try {
				camera.setPreviewDisplay(mSurfaceHolder);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			CameraParameters.setCameraParameters(camera, 
					previewSurface.getWidth(), previewSurface.getHeight());
	        
	        if(!Config.USE_ONE_SHOT_PREVIEW) {
	        	camera.setPreviewCallback(cameraHandler);	 
	        } 
			try {
				cameraHandler.init(camera);
			} catch (Exception e) {
				e.printStackTrace();
			}			
    	}
    }
    
    private void closeCamera() {
        if (camera != null) {
        	CameraHolder.instance().keep();
        	CameraHolder.instance().release();
        	camera = null;
        	camStatus.previewing = false;
        }
    }
    
    /**
     * Open the camera and start detecting markers.
     * note: You must assure that the preview surface already exists!
     */
    public void startPreview() {
    	if(!surfaceCreated) return;
    	if(mPausing || isFinishing()) return;
    	if (camStatus.previewing) stopPreview();
		System.out.println("opning camera");
		openCamera();
		camera.startPreview();
		camStatus.previewing = true;
    }
    
    /**
     * Close the camera and stop detecting markers.
     */
    private void stopPreview() {
    	if (camera != null && camStatus.previewing ) {
    		camStatus.previewing = false;
            camera.stopPreview();
         }
    	
    }

	/* The GLSurfaceView changed
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	/* The GLSurfaceView was created
	 * The camera will be opened and the preview started 
	 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceCreated = true;			
	}

	/* GLSurfaceView was destroyed
	 * The camera will be closed and the preview stopped.
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
	
	/**
	 * @return  a the instance of the ARToolkit.
	 */
	public ARToolkit getArtoolkit() {
		return artoolkit;
	}	
	
	/**
	 * Take a screenshot. Must not be called from the GUI thread, e.g. from methods like
	 * onCreateOptionsMenu and onOptionsItemSelected. You have to use a asynctask for this purpose.
	 * @return the screenshot
	 */
	public Bitmap takeScreenshot() {
		return renderer.takeScreenshot();
	}	
	
	/**
	 * 
	 * @return the OpenGL surface.
	 */
	public SurfaceView getSurfaceView() {
		return glSurfaceView;
	}
	
	class Preview extends SurfaceView implements SurfaceHolder.Callback {
	    SurfaceHolder mHolder;
	    Camera mCamera;
	    private int w;
	    private int h;
	    
	    Preview(Context context) {
	        super(context);
	        
	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }

	    public void surfaceCreated(SurfaceHolder holder) {
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // Surface will be destroyed when we return, so stop the preview.
	        // Because the CameraDevice object is not a shared resource, it's very
	        // important to release it when the activity is paused.
	        stopPreview();
	        closeCamera();
	        mSurfaceHolder = null;
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	    	this.w=w;
	    	this.h=h;
	    	mSurfaceHolder = holder;
	    	if(startPreviewRightAway)
	    		startPreview();
	    }
	    
	    public int getScreenWidth() {
	    	return w;
	    }
	    
	    public int getScreenHeight() {
	    	return h;
	    }

	}
}