package TerrainBase;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/**
 * 
 * @author Phil Larby
 * Extension of main app entry point to draw individual cubes from marhcing cubes lookups
 */
public class CubeBase extends PApplet{

	//Application entry point
	public static void main(String args[]) 
	{	
		//Initialise this as the entry point for the processing applet 
		//PApplet.main(new String[] { "--present", "TerrainBase.ProcBase" });
		String[] a = {"MAIN"};
		PApplet.runSketch(a, new CubeBase());
	}
	
	Cell[] cubes; 
	float YRot = 0;
	
	public void settings()
	{
		this.size(1024, 1024, PConstants.P3D);
	}
	
	/**
	 * Runs once on app initialisation
	 */
	@Override
	public void setup()
	{
		Helpers.Cam = new Camera(this);
		Helpers.Cam.setViewHeight(40);
		Helpers.P = this;
		
		Helpers.DrawCellVertex = true;
		Helpers.DrawCellTriangles = true;
		Helpers.DrawTriangleMesh = true;
		
		//Instantiate a new cube for each of the 256 possible vertex configurations 
		cubes = new Cell[256];
		for (int i = 0; i < 256; i++)
			cubes[i] = new Cell(i);
		
		//Initalise the lookup tables for surface extraction
		Surface.BuildEdgeBisectionTable();
				
	}
	
	/**
	 * Logic for draw cycle
	 */
	@Override
	public void draw()
	{
		
		Helpers.Cam.set(this);
		background(150);
		
		float spacing = 2.5f;
		
		//Layout and draw cubes
		for (int i = 0; i < 256; i++)
		{
			int x = (i % 16);
			int y = (int)Math.floor(i / 16);
			
			pushMatrix();
			translate(x * spacing - 8 * spacing, y * spacing - 8 * spacing);
			rotateY(YRot);
			cubes[i].draw(this);
			popMatrix();
		}
		
		YRot += 0.05;
	}
}
