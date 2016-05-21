package TerrainBaseTest;

import org.junit.Assert;

import java.util.ArrayList;
import org.junit.Test;
import TerrainBase.Helpers;

public class HelpersTest {

	@Test
	//Permute returns all permutations of integer array
	public void IdentifyCorrectNumberOfIntegerArrayPermutation() {
		
		ArrayList<Integer[]> actual = Helpers.permute(new int[] {1,2,3});
		
		Assert.assertEquals(actual.size(), 6);
	}
}
