package TerrainBase;

import processing.core.PVector;

/**
 * Extension of Processing's native PVector class intended for use as a vertex and adds the following: <br>
 * <ul>
 * <li>The ability to store a measure of the surface density at the vertex location </li>
 * <li>Tracks positon in both absoloute words space and local space realative to parent cell centre </li>
 * <li>Allows grouping identifiers which can be used to track realted vertices </li>
 * </ul>
 * Implements Comprable interface which facilitates default sorting in group id order
 * @author Philip Larby - Student number 070414017
 */
public class DensityVertex extends PVector implements Comparable<DensityVertex>
{
	private float _density = -1;
	private float _camDistance = -1; //Distance from camera to vertex is used for ordering
	private short _group = -1; 		 //By assigning vertices into adjacency groups it is easier to calculate triangle strips 
	private short _vertexID; 		 //Identifer of the vertex - refer to figure in documentation
	private Cell _cell;

	/**
	 * Constructs a new density vertex object
	 * @param x X position relative to parent cell centre point
	 * @param y Y position relative to parent cell centre point
	 * @param z Z position relative to parent cell centre point
	 * @param Id Vertex number identifying position in cell - as specified in project documentation
	 * @param c Reference to a the parent cell
	 */
	public DensityVertex(float x, float y, float z, int Id, Cell c) 
	{
		super(x, y, z);
		_vertexID = (short)Id;
		_cell = c;
	}
	
	/**
	 * Get the position of the vertex as absoloute world coordinates 
	 * @return the offset of the vertex from the world origin
	 */
	public PVector getAbsoloutePostion()
	{
		//Add the relative position of the vertex to the absoloute position of it's parent cell 
		PVector ret = _cell.getAbsolutePosition();
		ret.add(this);
		
		return ret;
	}
	
	/**
	 * Get the group identifier for the vertex
	 * @return The identifier for the Vertex's group.  -1 indicates no grouping
	 */
	public int getGroup()
	{
		return _group;
	}
	
	/**
	 * Set the group ID
	 * @param GroupID The identifier to be used for this group
	 */
	public void setGroup(short GroupID)
	{
		_group = GroupID;
	}

	/**
	 * Get the surface density at the vertex position
	 * @return the surface density 
	 */
	public float getDensity()
	{
		//lazy load density as needed to reduce overhead through multiple calls 
		if (_density == -1)
			_density = Helpers.Density(this, _cell);
		
		return _density;
	}
	
	/**
	 * Get the identifier for the vertex
	 * @return The position of the vertex in the structure of the parent cell as defined in the project documentation
	 */
	public short getId()
	{
		return _vertexID;
	}
	
	/**
	 * Set the X position of the vertex relative to the centre point of it's parent cell
	 * @param x Relative X position
	 */
	public void x (float x)
	{
		super.x = x;
		_density = -1;
	}
	
	/**
	 * Set the Y position of the vertex relative to the centre point of it's parent cell
	 * @param y Relative Y position
	 */
	public void y (float y)
	{
		super.y = y;
		_density = -1;
	}
	
	/**
	 * Set the Z position of the vertex relative to the centre point of it's parent cell
	 * @param z Relative Z position
	 */
	public void z (float z)
	{
		super.z = z;
		_density = -1;
	}
	
	/**
	 * execute update cycle logic
	 */
	public void update()
	{
		updateCamDistance(Helpers.Cam.getCam());
	}
	
	//Update the measure of the vertex from the position of the camera
	private void updateCamDistance(PVector CameraPosition)
	{
		_camDistance = PVector.dist(CameraPosition, getAbsoloutePostion());
	}

	/**
	 * Comparable interface member.  Facilitates sorting vertices by group order. 
	 */
	@Override
	public int compareTo(DensityVertex o) {
		
		//If values are equal return 0
		if (this.getGroup() == o.getGroup()) return 0;
		
		//If not equal compare whether greater than or less than
		return(this.getGroup() < o.getGroup() ? -1 : 1);
	}

	/**
	 * Set the density value for the vertex
	 * @param Density The density value to be recorded
	 */
	public void setDensity(int Density) {
		_density = Density;
		
	}
}
