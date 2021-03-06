package TerrainBaseTest;
import org.junit.Assert;
import org.junit.Test;
import TerrainBase.Cell;
import TerrainBase.DensityVertex;
import TerrainBase.Surface;
import processing.core.PVector;

public class CellTest {

	@Test
	//Build a cell with a specified bit patterns and confirm the identifier is reported as expected
	public void ValidateCellIdentifier() {
		//Arrange
		//-------
		
		//Create a cell with vertices 0, 1, 2, and 7 marked as inside the surface
		//Bit patterns for this cell configuration = 010000111
		//Integer value for this cell = 135
		int expected = 135;
		int actual;
		
		
		//Act
		//---
		Cell c = new Cell(expected);
		
		//Assert
		//------
		actual = c.getIdentifier();
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	//Build a cell with a specified bit patterns and confirm the identifier is reported as expected
	public void ValidateRandomCellIdentifier() {
		//Arrange
		//-------
		
		//Create a cell with a random vertex selection
		int expected = (int)(Math.random() * 255);
		int actual;
		
		
		//Act
		//---
		Cell c = new Cell(expected);
		
		//Assert
		//------
		actual = c.getIdentifier();
		Assert.assertEquals(expected, actual);
	}

	@Test
	//Build a cell with a specified bit patterns and confirm the expected vertices are reporting as inside the surface
	public void ValidateVertexDensities() {
		//Arrange
		//-------
		
		//Create a cell with vertices 0, 1, 2, and 7 marked as inside the surface
		//Bit patterns for this cell configuration = 010000111
		//Integer value for this cell = 135
		int VertexPattern = 135;
		
		//Act
		//---
		Cell c = new Cell(VertexPattern);
		
		//Assert
		//------
		
		//Validate all internal vertices
		Assert.assertTrue("Vertex 0 reporting as outisde surface", c.VertexInside(0));
		Assert.assertTrue("Vertex 1 reporting as outisde surface", c.VertexInside(1));
		Assert.assertTrue("Vertex 2 reporting as outisde surface", c.VertexInside(2));
		Assert.assertTrue("Vertex 7 reporting as outisde surface", c.VertexInside(7));
		
		//Validate all external vertices
		Assert.assertTrue("Vertex 3 reporting as inside surface", !c.VertexInside(3));
		Assert.assertTrue("Vertex 4 reporting as inside surface", !c.VertexInside(4));
		Assert.assertTrue("Vertex 5 reporting as inside surface", !c.VertexInside(5));
		Assert.assertTrue("Vertex 6 reporting as inside surface", !c.VertexInside(6));
	}
	
	@Test
	//Build a cell with a specified bit patterns and confirm the vertices are grouped as expected
	public void ValidateVertexGrouping() {
		//Arrange
		//-------
		
		//Create a cell with vertices 0, 1, 2, and 7 marked as inside the surface
		//Bit patterns for this cell configuration = 010000111
		//Integer value for this cell = 135
		int VertexPattern = 135;
		
		//Act
		//---
		
		Cell c = new Cell(VertexPattern);
		
		//Assert
		//------
		
		//Validate vertices 0, 1, and 2 have a grouping and are in the same group
		int group1 = c.getVertex(0).getGroup();
		Assert.assertTrue("Vertex 0 is ungrouped", group1 != -1);
		Assert.assertEquals( "Vertex 1 in different group to Vertex 0", group1, c.getVertex(1).getGroup());
		Assert.assertEquals( "Vertex 2 in different group to Vertex 0", group1, c.getVertex(2).getGroup());
	
		//Validate Vertex 7 is in it's own group
		int group2 = c.getVertex(7).getGroup();
		Assert.assertTrue("Vertex 7 is ungrouped", group2 != -1);
		Assert.assertTrue("Vertex 7 reporting in the same group as vertices 0,1, and 2", group1 != group2);
		
		//Validate remaining vertices are ungrouped
		Assert.assertTrue("Vertex 3 is grouped", c.getVertex(3).getGroup() == -1);
		Assert.assertTrue("Vertex 4 is grouped", c.getVertex(4).getGroup() == -1);
		Assert.assertTrue("Vertex 5 is grouped", c.getVertex(5).getGroup() == -1);
		Assert.assertTrue("Vertex 6 is grouped", c.getVertex(6).getGroup() == -1);
	}
	
	@Test
	//Build a cell with a specified bit patterns and confirm the vertices are grouped as expected
	public void ValidateVertexGroupCount() {
		//Arrange
		//-------
		
		//Create a cell with vertices 0, 1, 2, and 7 marked as inside the surface
		//Bit patterns for this cell configuration = 010000111
		//Integer value for this cell = 135
		int VertexPattern = 135;
		
		//Act
		//---
		
		Cell c = new Cell(VertexPattern);
		
		//Assert
		//------
		
		//Validate that 2 distinct vertex groups are created
		Assert.assertEquals( "The expected number of 2 vertex groups was not reported", c.vertexGroupCount(), 2);
	}
	
	@Test
	//Test direct access to vertex groups returns correct group members
	public void ValidateVertexGroupMembers() {
		//Arrange
		//-------
		
		//Create a cell with vertices 0, 1, 2, and 7 marked as inside the surface
		//Bit patterns for this cell configuration = 010000111
		//Integer value for this cell = 135
		int VertexPattern = 135;
		
		//Act
		//---
		
		Cell c = new Cell(VertexPattern);
		
		//Assert
		//------
		
		//Get the vertex groups = there should only be two
		Assert.assertEquals("Incorrect number of vertex groups reported", c.vertexGroupCount(), 2);
		DensityVertex[] group0 = c.getVertexGroup(0);
		DensityVertex[] group1 = c.getVertexGroup(1);
		
		//Create vertices to identify the groups based on member count
		DensityVertex[] g3 = new DensityVertex[3];
		DensityVertex[] g1 = new DensityVertex[1];
		
		//The group with three members should contain vertices 0, 1 and 2 and the group with one members should contain vertex 7
		if (group0.length == 3 && group1.length == 1)
		{
			g3 = group0;
			g1 = group1;
		}
		else if (group0.length == 1 && group1.length == 3)
		{
			g3 = group1;
			g1 = group0;
		}
		else
			Assert.fail("Incorrect group members reported");
		
		//Check group 1 members
		for(DensityVertex v : g1)
			Assert.assertEquals(7, v.getId());
		
		//Check group 2 members
		for(DensityVertex v : g3)
			Assert.assertTrue(v.getId() == 0 || v.getId() == 1 || v.getId() == 2);
	}
	
	@Test
	//Test that the centre points of the cell edges are correctly identified
	public void ValidateCorrectEdgeCentrePointsAreReported() {
		
		//Arrange
		//-------
		
		//Create a cell with vertices 0, 1, 2, and 7 marked as inside the surface
		//Bit patterns for this cell configuration = 010000111
		//Integer value for this cell = 135
		int VertexPattern = 135;
		
		//Act
		//---
		
		Cell c = new Cell(VertexPattern);
		
		//Assert
		//------
		
		//Validate all edge centres are reporting in the correct relative position
		//Edge 0
		Assert.assertEquals("Edge 0 X", 0f, c.getEdgeCentreVertex(0).x, 0);
		Assert.assertEquals("Edge 0 Y", 0.5f, c.getEdgeCentreVertex(0).y, 0);
		Assert.assertEquals("Edge 0 Z", -0.5f, c.getEdgeCentreVertex(0).z, 0);
		
		//Edge 1
		Assert.assertEquals("Edge 1 X", 0.5f, c.getEdgeCentreVertex(1).x, 0);
		Assert.assertEquals("Edge 1 Y", 0.5f, c.getEdgeCentreVertex(1).y, 0);
		Assert.assertEquals("Edge 1 Z", 0f, c.getEdgeCentreVertex(1).z, 0);
		
		//Edge 2
		Assert.assertEquals("Edge 2 X", 0f, c.getEdgeCentreVertex(2).x, 0);
		Assert.assertEquals("Edge 2 Y", 0.5f, c.getEdgeCentreVertex(2).y, 0);
		Assert.assertEquals("Edge 2 Z", 0.5f, c.getEdgeCentreVertex(2).z, 0);
		
		//Edge 3
		Assert.assertEquals("Edge 3 X", -0.5f, c.getEdgeCentreVertex(3).x, 0);
		Assert.assertEquals("Edge 3 Y", 0.5f, c.getEdgeCentreVertex(3).y, 0);
		Assert.assertEquals("Edge 3 Z", 0f, c.getEdgeCentreVertex(3).z, 0);
		
		//Edge 4
		Assert.assertEquals("Edge 4 X", -0.5f, c.getEdgeCentreVertex(4).x, 0);
		Assert.assertEquals("Edge 4 Y", 0f, c.getEdgeCentreVertex(4).y, 0);
		Assert.assertEquals("Edge 4 Z", -0.5f, c.getEdgeCentreVertex(4).z, 0);
		
		//Edge 5
		Assert.assertEquals("Edge 5 X", 0.5f, c.getEdgeCentreVertex(5).x, 0);
		Assert.assertEquals("Edge 5 Y", 0f, c.getEdgeCentreVertex(5).y, 0);
		Assert.assertEquals("Edge 5 Z", -0.5f, c.getEdgeCentreVertex(5).z, 0);
		
		//Edge 6
		Assert.assertEquals("Edge 6 X", 0.5f, c.getEdgeCentreVertex(6).x, 0);
		Assert.assertEquals("Edge 6 Y", 0f, c.getEdgeCentreVertex(6).y, 0);
		Assert.assertEquals("Edge 6 Z", 0.5f, c.getEdgeCentreVertex(6).z, 0);
		
		//Edge 7
		Assert.assertEquals("Edge 7 X", -0.5f, c.getEdgeCentreVertex(7).x, 0);
		Assert.assertEquals("Edge 7 Y", 0f, c.getEdgeCentreVertex(7).y, 0);
		Assert.assertEquals("Edge 7 Z", 0.5f, c.getEdgeCentreVertex(7).z, 0);
		
		//Edge 8
		Assert.assertEquals("Edge 8 X", 0.5f, c.getEdgeCentreVertex(8).x, 0);
		Assert.assertEquals("Edge 8 Y", -0.5f, c.getEdgeCentreVertex(8).y, 0);
		Assert.assertEquals("Edge 8 Z", 0f, c.getEdgeCentreVertex(8).z, 0);
		
		//Edge 9
		Assert.assertEquals("Edge 9 X", 0f, c.getEdgeCentreVertex(9).x, 0);
		Assert.assertEquals("Edge 9 Y", -0.5f, c.getEdgeCentreVertex(9).y, 0);
		Assert.assertEquals("Edge 9 Z", 0.5f, c.getEdgeCentreVertex(9).z, 0);
		
		//Edge 10
		Assert.assertEquals("Edge 10 X", -0.5f, c.getEdgeCentreVertex(10).x, 0);
		Assert.assertEquals("Edge 10 Y", -0.5f, c.getEdgeCentreVertex(10).y, 0);
		Assert.assertEquals("Edge 10 Z", 0f, c.getEdgeCentreVertex(10).z, 0);
		
		//Edge 11
		Assert.assertEquals("Edge 11 X", 0f, c.getEdgeCentreVertex(11).x, 0);
		Assert.assertEquals("Edge 11 Y", -0.5f, c.getEdgeCentreVertex(11).y, 0);
		Assert.assertEquals("Edge 11 Z", -0.5f, c.getEdgeCentreVertex(11).z, 0);
	}
	
	@Test
	//Check the correct edge bisections are identified for a single vertex cell pattern
	public void CheckVertex0BisectedEdges() {
		//Arrange
		//-------
		
		//Create a cell with vertex 0 marked as inside the surface
		//Bit patterns for this cell configuration = 000000001
		//Integer value for this cell = 1
		short VertexPattern = 1;
		
		//Expected bisected edges are 0, 3, 4
		//Binary = 11001
		//Decimal = 25
		int expected = Short.parseShort("00011001", 2);;
		
		//Act
		//---
		int actual = Surface.getBisectedEdges(VertexPattern);
		
		//Assert
		//-----
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	//Check the correct edge bisections are identified for a single vertex cell pattern
	public void CheckVertex5BisectedEdges() {
		//Arrange
		//-------
		
		//Create a cell with vertex 0 marked as inside the surface
		//Bit patterns for this cell configuration = 000000001
		//Integer value for this cell = 1
		short VertexPattern = Short.parseShort("00100000", 2);
		
		//Expected bisected edges are 5, 8, 11
		//Binary = 100100100000
		//Decimal = 2336
		int expected = 2336;
		
		//Act
		//---
		int actual = Surface.getBisectedEdges(VertexPattern);
		
		//Assert
		//-----
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	//Check the correct edge bisections are identified for a multiple adjacent vertex cell pattern
	public void CheckAdjacentVertexBisectedEdges() {
		//Arrange
		//-------
		
		//Create a cell with vertex 0 and 3 marked as inside the surface
		//Bit patterns for this cell configuration = 000001001
		//Integer value for this cell = 9
		short VertexPattern = 9;
		
		//Expected bisected edges are 0, 2, 4, 7
		//Binary = 10010101
		//Decimal = 149
		int expected = 149;
		
		//Act
		//---
		int actual = Surface.getBisectedEdges(VertexPattern);
		
		//Assert
		//-----
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	//Check the correct edge bisections are identified for a multiple adjacent vertex cell pattern
	public void CheckGroupedVertexBisectedEdges() {
		//Arrange
		//-------
		
		//Create a cell with 2 vertex groups 
		//Group 1: vertex 0 and 3 marked as inside the surface
		//Group 2: vertex 5
		//Bit patterns for this cell configuration = 000101001
		//Integer value for this cell = 41
		short VertexPattern = 41;
		
		//Expected bisected edges are:
		//Group 1: 0, 2, 4, 7
		//Group 2: 5, 8, 11
		//Binary = 100110110101
		//Decimal = 2485
		int expected = 2485;
		
		//Act
		//---
		int actual = Surface.getBisectedEdges(VertexPattern);
		//001110110101
		//100110110101
		
		//Assert
		//-----
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	//Check the correct edge bisections are identified for a multiple adjacent vertex cell pattern
	public void CheckTriangleVerticesForSingleVertexCell() {
		//Arrange
		//-------
		
		//Create a cell with a single internal vertex as position 0
		//Bit patterns for this cell configuration = 000000001
		//Integer value for this cell = 1
		short VertexPattern = Short.parseShort("000000001", 2);
		Cell c = new Cell(VertexPattern);
		
		//Act
		//---
		
		//Get the cells internal vertex
		DensityVertex[] cubeVerts = c.getVertexGroup(0);
		
		//Get triangle vertices
		PVector[] TriangleVerts = c.buildTriangleStrip(cubeVerts);
		
		
		//Assert
		//-----

		//Confirm the cell has 3 triangle vertices
		Assert.assertEquals(3, TriangleVerts.length);
	}
}
