/* class will take a vertex object and return the boolean prescence of  features in an array */

public class GetSpotFeatures {
	ResourceTranslator translator = new ResourceTranslator();
	GraphController graph;
	int scarcestResource;
	int mostCommonResource;
	/*
	 * 0 = rock
	 * 1 = wheat
	 * 2 = brick
	 * 3 = wood
	 * 4 = sheep	 
	 * 5 = on2
	 * 6 = on3
	 * 7 = on4
	 * 8 = on5
	 * 9 = on6
	 * 10 = on8
	 * 11 = on9
	 * 12 = on10
	 * 13 = on11
	 * 14 = on12
	 * 15 = oneDot
	 * 16 = twoDot
	 * 17 = threeDot
	 * 18 = fourDot
	 * 19 = fiveDot
	 * 20 = touches 3 different Resouces
	 * 21 = touches 2 different resources
	 * 22 = touches 1 different resource
	 * 23 = on 3 tiles
	 * 24 = on 2 tiles
	 * 25 = on 1 tile
	 * 26 = has >=4 builable spots
	 * 27 = has <2 buildable spots
	 * 28 = on Rock Port
	 * 29 = on Wheat Port
	 * 30 = on Brick Port
	 * 31 = on Wood Port
	 * 32 = on Sheep Port
	 * 33 = on 3 to 1 port
	 * 34 = on 2 or 12 wood
	 * 35 = on 2 or 12 brick
	 * 36 = on 2 or 12 stone
	 * 37 = on 2 or 12 wheat
	 * 38 = on 2 or 12 sheep
	 * 39 = on 3 or 11 wood
	 * 40 = on 3 or 11 brick
	 * 41 = on 3 or 11 stone
	 * 42 = on 3 or 11 wheat
	 * 43 = on 3 or 11 sheep
	 * 44 = on 4 or 10 wood
	 * 45 = on 4 or 10 brick
	 * 46 = on 4 or 10 stone
	 * 47 = on 4 or 10 wheat 
	 * 48 = on 4 or 10 sheep
	 * 49 = on 5 or 9 wood
	 * 50 = on 5 or 9 brick
	 * 51 = on 5 or 9 stone
	 * 52 = on 5 or 9 wheat
	 * 53 = on 5 or 9 sheep
	 * 54 = on 6 or 8 wood
	 * 55 = on 6 or 8 wheat
	 * 56 = on 6 or 8 brick
	 * 57 = on 6 or 8 stone
	 * 58 = on 6 or 8 sheep
	 * 59 =  rock is scarce
	 * 60 = wheat is scarce
	 * 61 = brick is scarce
	 * 62 = wood is scarce
	 * 63 = sheep is scarce
	 * 64 = rock is plentiful
	 * 65 = wheat is plentiful
	 * 66 = brick is plentiful
	 * 67 = wood is plentiful
	 * 68 = sheep is plentiful 
	 * 69 = built on scarcest
	 * 70 = built on most plentiful
	 * 
	 */
	
	public int[] getFeaturesForVertex (CatanVertex v, GraphController g){
		graph = g;
		boolean debug = false;
		int[] toReturn = new int[71];
		calculateScarcestResource();
		
		if (rock(v)){
			toReturn[0] = 1;
		}
		if (wheat(v)){
			toReturn[1] = 1;
		}
		if (brick(v)){
			toReturn[2] = 1;
		}
		if (wood(v)){
			toReturn[3] = 1;
		}
		if (sheep(v)){
			toReturn[4] = 1;
		}
		if (on2(v)){
			toReturn[5] = 1;
		}
		if (on3(v)){
			toReturn[6] = 1;
		}
		if (on4(v)){
			toReturn[7] = 1;
		}
		if (on5(v)){
			toReturn[8] = 1;
		}
		if (on6(v)){
			toReturn[9] = 1;
		}
		if (on8(v)){
			toReturn[10] = 1;
		}
		if (on9(v)){
			toReturn[11] = 1;
		}
		if (on10(v)){
			toReturn[12] = 1;
		}
		if (on11(v)){
			toReturn[13] = 1;
		}
		if (on12(v)){
			toReturn[14] = 1;
		}
		if (oneDot(v)){
			toReturn[15] = 1;
		}
		if (twoDot(v)){
			toReturn[16] = 1;
		}
		if (threeDot(v)){
			toReturn[17] = 1;
		}
		if (fourDot(v)){
			toReturn[18] = 1;
		}
		if (fiveDot(v)){
			toReturn[19] = 1;
		}
		if (touchesThreeDiffResources(v) ){
			toReturn[20] = 1;
		}
		if (touchesTwoDiffResources(v)){
			toReturn[21] = 1;
		}
		if (touchesOneTypeOfResource(v)){
			toReturn[22] = 1;
		}
		if (onThreeTiles(v)){
			toReturn[23] = 1;
		}
		if (onTwoTiles(v)){
			toReturn[24] = 1;
		}
		if (onOneTile(v)){
			toReturn[25] = 1;
		}
		if (hasAtLeast4BuildableSpots(v)){
			toReturn[26] = 1;
		}
		if (doesNotHave2BuildableSpots(v)){
			toReturn[27] = 1;
		}
		if (onRockPort(v)){
			toReturn[28] = 1;
		}
		if (onWheatPort(v)){
			toReturn[29] = 1;
		}
		if (onBrickPort(v)){
			toReturn[30] = 1;
		}
		if (onWoodPort(v)){
			toReturn[31] = 1;
		}
		if (onSheepPort(v)){
			toReturn[32] = 1;
		}
		if (on3to1Port(v)){
			toReturn[33] = 1;
		}
		if (on2and12wood(v)){
			toReturn[34] = 1;
		}
		if (on2and12brick(v)){
			toReturn[35] = 1;
		}
		if (on2and12stone(v)){
			toReturn[36] = 1;
		}
		if (on2and12wheat(v)){
			toReturn[37] = 1;
		}
		if (on2and12sheep(v)){
			toReturn[38] = 1;
		}
		if (on3and11wood(v)){
			toReturn[39] = 1;
		}
		if (on3and11brick(v)){
			toReturn[40] = 1;
		}
		if (on3and11stone(v)){
			toReturn[41] = 1;
		}
		if (on3and11wheat(v)){
			toReturn[42] = 1;
		}
		if (on3and11sheep(v)){
			toReturn[43] = 1;
		}
		if (on4and10wood(v)){
			toReturn[44] = 1;
		}
		if (on4and10brick(v)){
			toReturn[45] = 1;
		}
		if (on4and10stone(v)){
			toReturn[46] = 1;
		}
		if (on4and10wheat(v)){
			toReturn[47] = 1;
		}
		if (on4and10sheep(v)){
			toReturn[48] = 1;
		}
		if (on5and9wood(v)){
			toReturn[49] = 1;
		}
		if (on5and9brick(v)){
			toReturn[50] = 1;
		}
		if (on5and9stone(v)){
			toReturn[51] = 1;
		}
		if (on5and9wheat(v)){
			toReturn[52] = 1;
		}
		if (on5and9sheep(v)){
			toReturn[53] = 1;
		}
		if (on6and8wood(v)){
			toReturn[54] = 1;
		}
		if (on6and8brick(v)){
			toReturn[55] = 1;
		}
		if (on6and8stone(v)){
			toReturn[56] = 1;
		}
		if (on6and8wheat(v)){
			toReturn[57] = 1;
		}
		if (on6and8sheep(v)){
			toReturn[58] = 1;
		}
		if (scarcestResource == translator.Rock){
			toReturn[59] = 1;
		}
		if (scarcestResource == translator.Wheat){
			toReturn[60] =1;
		}
		if (scarcestResource == translator.Brick){
			toReturn[61] = 1;
		}
		if (scarcestResource == translator.Wood){
			toReturn[62] =1 ;
		}
		if (scarcestResource == translator.Sheep){
			toReturn[63] = 1;
		}
		if (mostCommonResource == translator.Rock){
			toReturn[64] = 1;
		}
		if (mostCommonResource == translator.Wheat){
			toReturn[65] = 1;
		}
		if (mostCommonResource == translator.Brick){
			toReturn[66] = 1;
		}
		if (mostCommonResource == translator.Wood){
			toReturn[67] = 1;
		}
		if (mostCommonResource == translator.Sheep){
			toReturn[68] = 1;
		}
		if (onScarceResource(v)){
			toReturn[69] = 1;
		}
		if (onPlentifulResource(v)){
			toReturn[70] = 1;
		}
		if (debug){
			System.out.println("features for vertex "+v.vertexNumber);
			for (int i = 0; i<toReturn.length; i++){
				System.out.print(i+": ");
				System.out.println(toReturn[i]);
			}
		}
		
		return toReturn;
	}
	
	private boolean rock(CatanVertex v){
		//check graph if that vertex is on rock. Return true if so
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].resource==translator.Rock){
				return true;
			}
		}
		return false;
	}
	
	private boolean wheat(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].resource==translator.Wheat){
				return true;
			}
		}
		return false;
	}
	
	private boolean brick(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].resource==translator.Brick){
				return true;
			}
		}
		return false;
	}
	
	private boolean wood(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].resource==translator.Wood){
				return true;
			}
		}
		return false;
	}

	
	private boolean sheep(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].resource==translator.Sheep){
				return true;
			}
		}
		return false;
	}
	
	private boolean on2(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==2){
				return true;
			}
		}
		return false;
	}
	
	private boolean on3(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==3){
				return true;
			}
		}
		return false;
	}

	private boolean on4(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==4){
				return true;
			}
		}
		return false;
	}

	private boolean on5(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==5){
				return true;
			}
		}
		return false;
	}
	private boolean on6(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==6){
				return true;
			}
		}
		return false;
	}

	private boolean on8(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==8){
				return true;
			}
		}
		return false;
	}

	private boolean on9(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==9){
				return true;
			}
		}
		return false;
	}

	private boolean on10(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==10){
				return true;
			}
		}
		return false;
	}

	private boolean on11(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==11){
				return true;
			}
		}
		return false;
	}

	private boolean on12(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].roll==12){
				return true;
			}
		}
		return false;
	}

	
	private boolean oneDot (CatanVertex v){
		if(on2(v) || on12(v)){
			return true;
		}
		return false;
	}
	
	private boolean twoDot (CatanVertex v){
		if(on3(v) || on11(v)){
			return true;
		}
		return false;
	}

	private boolean threeDot (CatanVertex v){
		if(on4(v) || on10(v)){
			return true;
		}
		return false;
	}
	
	private boolean fourDot (CatanVertex v){
		if(on5(v) || on9(v)){
			return true;
		}
		return false;
	}
	private boolean fiveDot (CatanVertex v){
		if(on6(v) || on8(v)){
			return true;
		}
		return false;
	}
	
	private boolean touchesThreeDiffResources (CatanVertex v){
		Tile[] tiles = v.getAdjacentTiles();
		int[] resourceCounts = new int[6];
		int numDiffResourcesOnSpot = 0;
		
		for (int i =0; i<tiles.length; i++){
			Tile t = tiles[i];
			int r = t.resource;
			if (resourceCounts[r] == 0){
				resourceCounts[r] ++;
				numDiffResourcesOnSpot ++;
			}
		}
		
		return numDiffResourcesOnSpot == 3;
	}
	
	private boolean touchesTwoDiffResources (CatanVertex v){
		Tile[] tiles = v.getAdjacentTiles();
		int[] resourceCounts = new int[6];
		int numDiffResourcesOnSpot = 0;
		
		for (int i =0; i<tiles.length; i++){
			Tile t = tiles[i];
			int r = t.resource;
			if (resourceCounts[r] == 0){
				resourceCounts[r] ++;
				numDiffResourcesOnSpot ++;
			}
		}
		
		return numDiffResourcesOnSpot == 2;
	}
	
	private boolean touchesOneTypeOfResource (CatanVertex v){
		Tile[] tiles = v.getAdjacentTiles();
		int[] resourceCounts = new int[6];
		int numDiffResourcesOnSpot = 0;
		
		for (int i =0; i<tiles.length; i++){
			Tile t = tiles[i];
			int r = t.resource;
			if (resourceCounts[r] == 0){
				resourceCounts[r] ++;
				numDiffResourcesOnSpot ++;
			}
		}
		
		return numDiffResourcesOnSpot == 1;
	}
	
	//false if you touch 2 or 3. 
	private boolean onThreeTiles (CatanVertex v){
		Tile[] tiles = v.getAdjacentTiles();
		int numResourcesOnSpot = 0;
		for (int i =0; i<tiles.length; i++){
			Tile t = tiles[i];
			int r = t.resource;
			if (r != 0){
				numResourcesOnSpot ++;
			}
		}
		return numResourcesOnSpot == 3;
	}
	
	private boolean onTwoTiles (CatanVertex v){
		Tile[] tiles = v.getAdjacentTiles();
		int numResourcesOnSpot = 0;
		for (int i =0; i<tiles.length; i++){
			Tile t = tiles[i];
			int r = t.resource;
			if (r != 0){
				numResourcesOnSpot ++;
			}
		}
		return numResourcesOnSpot == 2;
	}
	
	private boolean onOneTile (CatanVertex v){
		Tile[] tiles = v.getAdjacentTiles();
		int numResourcesOnSpot = 0;
		for (int i =0; i<tiles.length; i++){
			Tile t = tiles[i];
			int r = t.resource;
			if (r != 0){
				numResourcesOnSpot ++;
			}
		}
		return numResourcesOnSpot == 1;
	}
	
	private boolean hasAtLeast4BuildableSpots (CatanVertex v){
		boolean debug2Away = false;
		int freeSpotCount = 0;
		CatanVertex [] twoAway = v.getVertices2Away();
		for (int i =0 ; i<twoAway.length; i++){
			if (twoAway[i].getSettlementType() == 0){
				freeSpotCount ++;
				if (debug2Away){
					System.out.println("At vertex: "+v.vertexNumber+" empty vertex "+twoAway[i].vertexNumber+" is 2 edges away");
				}
			}
		}
		return freeSpotCount >=4;
	}
	
	private boolean doesNotHave2BuildableSpots (CatanVertex v){
		boolean debug2Away = false;
		int freeSpotCount = 0;
		CatanVertex [] twoAway = v.getVertices2Away();
		for (int i =0 ; i<twoAway.length; i++){
			if (twoAway[i].getSettlementType() == 0){
				freeSpotCount ++;
				if (debug2Away){
					System.out.println("At vertex: "+v.vertexNumber+" empty vertex "+twoAway[i].vertexNumber+" is 2 edges away");
				}
			}
		}
		return freeSpotCount <2;
		}
	
	private boolean onRockPort(CatanVertex v){
		Tile port = v.getPort();
		if (port != null){
			if (port.portType==1){
				return true;
			}
		}
		return false;
	}
	private boolean onWheatPort(CatanVertex v){
		Tile port = v.getPort();
		if (port != null){
			if (port.portType==2){
				return true;
			}
		}
		return false;
	}	
	private boolean onBrickPort(CatanVertex v){
		Tile port = v.getPort();
		if (port != null){
			if (port.portType==3){
				return true;
			}
		}
		return false;
	}
	private boolean onWoodPort(CatanVertex v){
		Tile port = v.getPort();
		if (port != null){
			if (port.portType==4){
				return true;
			}
		}
		return false;
	}	
	private boolean onSheepPort(CatanVertex v){
		Tile port = v.getPort();
		if (port != null){
			if (port.portType==5){
				return true;
			}
		}
		return false;
	}	
	
	private boolean on3to1Port(CatanVertex v){
		Tile port = v.getPort();
		if (port != null){
			if (port.portType==0){
				return true;
			}
		}
		return false;
	}
	
	private int[] getRollandResource(Tile t){

		int[] toReturn = new int[2];
		Tile tiles = t;	
		if(tiles.roll==2){
			toReturn[0]=2;
		}
		if(tiles.roll==3){
			toReturn[0]=3;
		}
		if(tiles.roll==4){
			toReturn[0]=4;
		}
		if(tiles.roll==5){
			toReturn[0]=5;
		}
		if(tiles.roll==6){
			toReturn[0]=6;
		}
		if(tiles.roll==8){
			toReturn[0]=8;
		}
		if(tiles.roll==9){
			toReturn[0]=9;
		}
		if(tiles.roll==10){
			toReturn[0]=10;
		}
		if(tiles.roll==11){
			toReturn[0]=11;
		}
		if(tiles.roll==12){
			toReturn[0]=12;
		}
		if(tiles.resource==translator.Rock){
			toReturn[1]=1;
		}
		if(tiles.resource==translator.Wheat){
			toReturn[1]=2;
		}
		if(tiles.resource==translator.Brick){
			toReturn[1]=3;
		}
		if(tiles.resource==translator.Wood){
			toReturn[1]=4;
		}
		if(tiles.resource==translator.Sheep){
			toReturn[1]=5;
		}
		return toReturn;
	}

	private boolean on2and12wood(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==2 || array[0]==12){
				if(array[1]==4){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on2and12brick(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==2 || array[0]==12){
				if(array[1]==3){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on2and12stone(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==2 || array[0]==12){
				if(array[1]==1){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on2and12wheat(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==2 || array[0]==12){
				if(array[1]==2){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on2and12sheep(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==2 || array[0]==12){
				if(array[1]==5){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on3and11wood(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==3 || array[0]==11){
				if(array[1]==4){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on3and11brick(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==3 || array[0]==11){
				if(array[1]==3){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on3and11stone(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==3 || array[0]==11){
				if(array[1]==1){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on3and11wheat(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==3 || array[0]==11){
				if(array[1]==2){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on3and11sheep(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==3 || array[0]==11){
				if(array[1]==5){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on4and10wood(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==4 || array[0]==10){
				if(array[1]==4){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on4and10brick(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==4 || array[0]==10){
				if(array[1]==3){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on4and10stone(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==4 || array[0]==10){
				if(array[1]==1){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on4and10wheat(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==4 || array[0]==10){
				if(array[1]==2){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on4and10sheep(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==4 || array[0]==10){
				if(array[1]==5){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on5and9wood(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==5 || array[0]==9){
				if(array[1]==4){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on5and9brick(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==5 || array[0]==9){
				if(array[1]==3){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on5and9stone(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==5 || array[0]==9){
				if(array[1]==1){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on5and9wheat(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==5 || array[0]==9){
				if(array[1]==2){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on5and9sheep(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==5 || array[0]==9){
				if(array[1]==5){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on6and8wood(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==6 || array[0]==8){
				if(array[1]==4){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on6and8brick(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==6 || array[0]==8){
				if(array[1]==3){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on6and8stone(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==6 || array[0]==8){
				if(array[1]==1){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on6and8wheat(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==6 || array[0]==8){
				if(array[1]==2){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	private boolean on6and8sheep(CatanVertex v){
		boolean toReturn=false;
		int[] array = new int[2];	
		Tile [] tiles = v.getAdjacentTiles();	
		for(int i=0; i<tiles.length; i++){
			array = getRollandResource(tiles[i]);
			if (array[0]==6 || array[0]==8){
				if(array[1]==5){
					toReturn = true;
				}
			}
		}
		return toReturn;
	}
	
	private void calculateScarcestResource(){
		scarcestResource = graph.getScarcestResource();
		mostCommonResource = graph.getMostBountifulResource();
	}
	
	private boolean onScarceResource(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].resource==scarcestResource){
				return true;
			}
		}
		return false;
	}
	
	private boolean onPlentifulResource(CatanVertex v){
		Tile [] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if(tiles[i].resource==mostCommonResource){
				return true;
			}
		}
		return false;
	}
	
	
}