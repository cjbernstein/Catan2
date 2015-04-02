/* JE
 * a class to translate the integer value of a resource to the string type
 */
public class ResourceTranslator {
	
	public int Rock = 1;
	public int Wheat = 2;
	public int Brick = 3;
	public int Wood = 4;
	public int Sheep = 5;
	
	public static String getType(int value){
		String toReturn = null;
		switch (value) {
			case 0: toReturn = 	"desert";
					break;
			case 1: toReturn = "rock";
					break;
			case 2: toReturn = "wheat";
					break;
			case 3: toReturn = "brick";
					break;
			case 4: toReturn = "wood";
					break;
			case 5: toReturn = "sheep";
					break;
		}
		return toReturn;
	}
	

}
