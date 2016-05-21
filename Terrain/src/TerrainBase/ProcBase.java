
package TerrainBase;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Entry class for main output of project.  This extends the processing core libraries from http://www.processing.org.  This
 * library and is used extensively throughout this project.
 * @author Philip Larby
 *
 */
public class ProcBase extends PApplet {
	
	/**
	 * Application entry point
	 * @param args No command line arguments are supported at present
	 */
	public static void main(String args[]) 
	{	
		//Initialise this as the entry point for the processing applet 
		//PApplet.main(new String[] { "--present", "TerrainBase.ProcBase" });
		String[] a = {"MAIN"};
		PApplet.runSketch(a, new ProcBase());
	}
	
	/**
	 * The scaffold is the starting-point for geometry generation.
	 */
	private Scaffold s;
	
	/**
	 * Runs once on initialisation prior to the standard processing setup method.  Used for configuring
	 * some global processing properties 
	 */
	public void settings()
	{
		this.size(1280, 1024, PConstants.P3D);
	}
	
	/**
	 * Runs once on initialisation after the settings method. Used for application specific initialisation operations. 
	 */
	public void setup()
	{		
		//Create a camera referenced in the helpers class
		Helpers.Cam = new Camera(this);
		
		Helpers.P = this;
		
		//Instantiate new scaffold;
		s = new Scaffold(Helpers.RootCellResolution);
		
		//Initalise generation of the the lookup tables for surface extraction processing
		Surface.BuildEdgeBisectionTable();
	}
	
	/**
	 * Logic for the update cycle which runs prior to each draw cycle
	 */
	private void update()
	{
		Helpers.SmallestCellDiameter = Helpers.EarthDiameter;
		
		//Execute child updates last to prevent null pointers due to dependencies on setup logic here
		s.update();
	}
	
	/**
	 * Logic for keypress events
	 */
	@Override
	public void keyPressed()
	{
		if (key == 'w') Helpers.DrawTriangleMesh = !Helpers.DrawTriangleMesh;
		if (key == 'p') Helpers.DrawPatchBounds = !Helpers.DrawPatchBounds;
		if (key == 'c') Helpers.DrawCellBounds = !Helpers.DrawCellBounds;
		if (key == 'v') Helpers.DrawCellVertex = !Helpers.DrawCellVertex;
		if (key == 'n') Helpers.DrawCellCentre = !Helpers.DrawCellCentre;
		if (key == 'o') Helpers.DrawScaffoldOrigin = !Helpers.DrawScaffoldOrigin;
		if (key == 'g') Helpers.DrawCellTriangles = !Helpers.DrawCellTriangles;
		if (key == 'r') s = new Scaffold(Helpers.RootCellResolution);
		if (key == '+') {Helpers.DensityThreshold += 0.01; s = new Scaffold(Helpers.RootCellResolution);}
		if (key == '-') {Helpers.DensityThreshold -= 0.01; s = new Scaffold(Helpers.RootCellResolution);}
		if (key == 'a') {Helpers.RootCellResolution += 1; s = new Scaffold(Helpers.RootCellResolution);}
		if (key == 'z') {Helpers.RootCellResolution -= 1; s = new Scaffold(Helpers.RootCellResolution);}
		if (key == 's') {Helpers.PatchResolution += 1;}
		if (key == 'x') {Helpers.PatchResolution -= 1;}
	}
	
	/**
	 * Draw cycle logic
	 */
	public void draw()
	{
		
		Helpers.DrawCycleStart = System.nanoTime();
		
		//Trigger update logic to run prior to draw logic
		update();
		
		//Set background as Black
	    background(0);
	    
	    //Apply lighting configuration
	    directionalLight(180, 180, 180, 0, 1, 0);
	    ambientLight(50, 50, 50);
	    lights();
	    
	    //Apply current camera configuration
  		Helpers.Cam.set(this);
	    
	    //trigger drawing process for child objects
	    s.draw(this);
	    
	    Helpers.DrawCycleEnd = System.nanoTime();
	    
	    Helpers.FPS = (int) (1/((Helpers.DrawCycleEnd - Helpers.DrawCycleStart) / 1000000000.0));
	    if (Helpers.FirstCycleTime == -1) Helpers.FirstCycleTime = (Helpers.DrawCycleEnd - Helpers.DrawCycleStart) / 1000000000.0;
	}
}
