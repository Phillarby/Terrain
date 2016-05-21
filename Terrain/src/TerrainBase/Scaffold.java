package TerrainBase;

import processing.core.PApplet;

public class Scaffold {
	Cell _rootCell;  //The root of the geometry tree
	Patch _rootPatch;//A root patch
	
	/**
	 * Create a default scaffold with the following properties: 
	 * position at the origin
	 * Diameter use the earth's average radius
	 * 8 subdivisions divisions
	 */
	public Scaffold(int Resolution)
	{
		//Call chained constructor using preset values
		this(0, 0, 0, Helpers.EarthDiameter, Helpers.EarthDiameter, Helpers.EarthDiameter, Resolution, Resolution, Resolution);		
	}
	
	/**
	 * Constructor specifying all construction parameters
	 * @param PosX X position
	 * @param PosY Y position
	 * @param PosZ Z position
	 * @param DimX X Size 
	 * @param DimY Y Size
	 * @param DimZ Z Size
	 * @param SubX X Subdivisions
	 * @param SubY Y Subdivisions
	 * @param SubZ Z Subdivisions
	 */
	public Scaffold(float PosX, float PosY, float PosZ, float DimX, float DimY, float DimZ, int SubX, int SubY, int SubZ)
	{
		
		Helpers.CellCount = 0;
		Helpers.PatchCount = 0;
		
		//Create root cell, which operates as a bounding box for the sphere
		Helpers.log(1,"Constructing root cell");
		_rootCell = new Cell(
              PosX, //X Position
              PosY, //Y Position
              PosZ, //Z Position
              DimX, //X Size
              DimY, //Y Size
              DimZ); //Z Size	
		Helpers.log(1,"Root cell construction complete");
		Helpers.log(1,"");
		
		//Create a root patch and inject into root cell
		Helpers.log(1,"Constructing root patch");
		_rootPatch = new Patch(SubX, SubY, SubZ, _rootCell);
		_rootCell.setChildPatch(_rootPatch);
		Helpers.log(1,"Root patch construction complete");
		Helpers.log(1,"");
	}

	/**
	 * Trigger update cycle logic
	 */
	public void update()
	{
		//Trigger update of the root cell
		_rootCell.update();
	}

	/**
	 * Trigger Draw cycle logic
	 */
	public void draw(PApplet P)
	{
		//Draw the centre point of the scaffold if specified
    	if (Helpers.DrawScaffoldOrigin)
	    {
		    P.strokeWeight(10);
		    P.stroke(0,0,255);
		    P.point(0, 0, 0);
	    }
    	
    	//Draw the root cell
		_rootCell.draw(P);
		
		Helpers.Cam.addHUD(P);
	}
}
