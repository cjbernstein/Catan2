import java.util.Random;

/*JE
 * The GraphController class will maintain the state of the graph as game play progresses.
 * Actions like: build settlement, build road, and build city will all be validated and carried out in this class.  
 */
public class GraphController {
	static CatanVertex[] vertices; //pointers to the vertex objects in graph (connected by edge objects)
	static Tile[] tiles;
	boolean debug = false; 
	
	public GraphController (CatanVertex[] v, Tile[] t){
		vertices = v;
		tiles = t;
	}
	
	/*check if it is legal to build a settlement here (ie- no other settlements at a neighbor vertex 
	 * and no settlement already at this vertex)
	 * If so return true
	 * if not return false
	 */
	public boolean checkPlaceSettlement(int v, Player p, boolean printError){
		CatanVertex vert = vertices[v];
		DecisionTree dt = new DecisionTree (this);
		dt.isSpotGood(v);
		if (vert.getSettlementType() != 0) { 
			//ensure vertex is empty
			if(printError){
				System.out.println("There is already a settlement on this vertex");
			}
			return false;
		} else {
			//check that position is legal
			Edge[] es = vert.getEdges();
			for (int i=0; i<vert.getNumEdges(); i++){
				/*
				System.out.println("Going thru edges of vertex "+v+" at index: "+i);
				System.out.println(es[i].v1.getSettlementType());
				System.out.println(es[i].v2.getSettlementType());
				*/
				if (es[i].v1.getSettlementType() != 0  || es[i].v2.getSettlementType() != 0){
					//there is another settlement one edge away(neighbor vertex)
					if(printError){
						System.out.println("There is a settlement next to this vertex");
					}
					return false;
				}
			}
			//position is legal, so build settlement
			return true;
		}
	}
	
	public void addSettlementToGraph(int v, Player p){
		CatanVertex vert = vertices[v];
		vert.buildSettlement(p);
	//	GetSpotFeatures feats = new GetSpotFeatures();
		//int[] features = feats.getFeaturesForVertex (vert);
	}
	
	/*
	 * will first check that the player has roads on an edge leading to this settlement,
	 * then will call placeSettlement
	 */
	public boolean checkBuildSettlement(int v, Player p, boolean printError){
		if (connectedRoads(v,p)){
			return checkPlaceSettlement(v,p, printError);
		} else{
			if(printError){
				System.out.println("You need roads connected to this vertex");
			}
			return false;
		}
	}
	
	/* 
	 * will return an array of edges that player can build a road to
	 */
	public Edge[] getPossibleEdges(Player p){
		return new Edge[2];
	}
	
	/*check if it is legal to build a city here (ie- Player p already has a settlement at this vertex)
	 * If so, place city and return true
	 * if not, make no change to the graph and return false
	 */
	public boolean checkBuildCity(int v, Player p, boolean printError){
		CatanVertex settlement = vertices[v];
		if (settlement.getSettlementType() == 1 && settlement.getOwner() == p){
			return true;
		} else{
			if(printError){
				System.out.println("Cannot build city at vertex with settlement type: "+settlement.getSettlementType()+" and current owner: "+settlement.getOwner());
			}
			return false;
		}
	}
	
	public void addCityToGraph(int v, Player p ){
		CatanVertex settlement = vertices[v];
		settlement.buildCity();
	}
	
	/*check if it is legal to place a road between given vertices
	 * If so, place road and return true
	 * if not, make no change to the graph and return false	
	*/
	public boolean checkPlaceRoad(int a, int b, Player p, boolean printError){
		//find edge object that links v1 and v2, then check if edge is free
		Edge toConsider = null;
		Edge[] e1 = vertices[a].getEdges();
		for (int i = 0; i<vertices[a].getNumEdges(); i++ ){
			/*
			System.out.println("Building road between: "+a+" and "+b);
			System.out.println("vertex1 of edge: ");
			e1[i].v1.printResources();
			System.out.println(" vertex 2 of edge ");
			e1[i].v2.printResources();
			*/
			//must find the edge object that has either combination of {v1,v2} = {a,b} or {b,a}
			if (e1[i].v1 == vertices[a] && e1[i].v2 == vertices[b] || 
					e1[i].v1 == vertices[b] && e1[i].v2 == vertices[a]){
				toConsider = e1[i];
			}
		}
		if (toConsider == null){
			//there is no edge that connects those two vertices, invalid input
			 if (printError){
				 System.out.println("These two vertices are not linked by one edge");
			 }
			return false;
		} else {
			 if (toConsider.hasRoad){
				 //there is already a road here!
				 if (printError){
					 System.out.println("There is already a road on this edge");
				 }
				 return false;
			 } else {
				 return true;
			 }
		}
	}
	
	public void addRoadToGraph(int a, int b, Player p){
		Edge toConsider = null;
		Edge[] e1 = vertices[a].getEdges();
		for (int i = 0; i<vertices[a].getNumEdges(); i++ ){
			if (e1[i].v1 == vertices[a] && e1[i].v2 == vertices[b] || 
					e1[i].v1 == vertices[b] && e1[i].v2 == vertices[a]){
				toConsider = e1[i];
			}
		}
		 toConsider.buildRoad(p);
	}
	
	/*
	 * will check that player has road connecting to either v1 or v2, then will call placeRoad
	 *TODO: incorporate longest road
	 */
	public boolean checkBuildRoad(int v1, int v2, Player p, boolean printErrors){
		if (connectedRoads(v1,p) || connectedRoads(v2,p)){
			return checkPlaceRoad (v1, v2, p, printErrors);
		} else{
			if (printErrors){
				System.out.println("Player "+p+" does not have a road leading to this vertex");
			}
			return false;
		}
	}
	 
	/*
	 * Method will check that player p has at least one road leading to vertex v
	 */
	private boolean connectedRoads(int v, Player p){
		Edge[] edges = vertices[v].getEdges();
			for (int i = 0; i<vertices[v].getNumEdges(); i++){
				if (edges[i].owner == p ){
					return true;
				}
			}
		return false;
	}
	
	 //must check for settlement at one edge - if in first round
	 public boolean checkPlaceRound1Road(int v1, int v2, Player p, boolean printErrors){
			CatanVertex vert1 = vertices[v1];
			CatanVertex vert2 = vertices[v2];
			if (vert1.getOwner()== p || vert2.getOwner() ==p ) {
				return checkPlaceRoad(v1,v2,p, printErrors);
			} else {
				if (printErrors){
					System.out.println("You must have a settlement at one end of this road");
				}
				return false;
			}
	 }
	 
	 public Edge[] getLegalRound1Roads(Player p){
		Edge[] possibleRoads = new Edge[6];
		int numAdded = 0;
		 for (int i = 0; i< 54; i++){
			 CatanVertex vert = vertices[i];
			 if (vert.getSettlementType() != 0){
				 if (vert.getOwner() == p){
					 Edge[] es = vert.getEdges();
					 boolean thisPlayerHasRoadFromVert = false;
					 for (int j = 0; j<es.length; j++){
						 if (es[j].hasRoad){
							 if (es[j].owner == p){
								 //System.out.println("In get legal road method. Player "+ p.getID()+" already has a raod from vertex "+i);
								 thisPlayerHasRoadFromVert = true;
							 }
						 }
					 }
					 if (! thisPlayerHasRoadFromVert ){
						 //go through edges and add the emptyOnes
						 for (int j = 0; j<es.length; j++){
							 if (!es[j].hasRoad){
								possibleRoads[numAdded] = es[j];
								numAdded ++;
							 }
						 }
					 }
				 }
			 }
		 }
		 
		 if (numAdded == possibleRoads.length){
			 return possibleRoads;
		 } else {
			 Edge[] toReturn = new Edge[numAdded];
			 for (int i = 0; i<numAdded; i++) {
				 toReturn[i] = possibleRoads[i];
			 }
			 return toReturn;
		 }
	 }
	
	private Player longestRoad(){
		int longestRoad = 0;
		for (int i = 0; i<vertices.length; i++){
			CatanVertex v1 = vertices[i];
			//impliment Stack
			
		}
		return vertices[1].getOwner();
	
	}
	
	/*
	 * Go through vertices and determine which players should receive resources for the given roll. 
	 * Allocate those resources using methods in the player class.
	 */

	public void distributeResources(int roll){
		for (int i =0; i<vertices.length; i++){
			CatanVertex v = vertices[i];
			if (v.getSettlementType() != 0){
				// this vertex contains either a settlement or a city
				Tile[] tiles = v.getAdjacentTiles();
				Player owner = v.getOwner();
				for (int j = 0; j<tiles.length; j++){
					if (tiles[j].roll == roll && !tiles[j].hasRobber){
						if (v.getSettlementType()==1){
							if (debug) {
								System.out.println("resource of type: "+Resource.getType(tiles[j].resource)+
										" given on roll"+ roll+ " to player"+owner.getID());
							}
							owner.addResource(tiles[j].resource, 1);
						} else if (v.getSettlementType() == 2){
							if (debug) {
								System.out.println("2 resources of type: "+Resource.getType(tiles[j].resource)+
										" given on roll"+ roll+ " to player" + owner.getID());
							}
							owner.addResource(tiles[j].resource, 2);
						}
					}
				}
			}
			
		}
	}
	
	/*
	 * Method that will give each owner the three resources of their second settlement
	 */
	public void firstRoundResource(int vertex){
		CatanVertex v = vertices[vertex];
		Player owner = v.getOwner();
		Tile[] tiles = v.getAdjacentTiles();
		for (int i=0; i<tiles.length; i++){
			if (debug) {
				System.out.println("resource of type: "+Resource.getType(tiles[i].resource)+
						" given to player "+owner.getID());
			}
			owner.addResource(tiles[i].resource, 1);
		}
	}
	
	/*
	 * Method that moves location of robber
	 */
	public Player moveRobber (int tile){
		for (int i =0; i<tiles.length; i++){
			if (tiles[i].hasRobber){
				tiles[i].hasRobber = false;
			}
		}
		tiles[tile].hasRobber = true;	
		Player[] playersOnTile = new Player[3];//maximum of 3 players on any tile
		int playersCount = 0;
		for (int i =0; i<vertices.length; i++){
			CatanVertex v = vertices[i];
			if (v.getSettlementType() != 0){
				// this vertex contains either a settlement or a city
				Tile[] tiles = v.getAdjacentTiles();
				Player owner = v.getOwner();
				for (int j = 0; j<tiles.length; j++){
					if (tiles[j].hasRobber){
						System.out.println("Settlement on tile "+tile);
						System.out.println("Owner of settlement +"+owner.getID());
						playersOnTile[playersCount] = owner;
						playersCount ++;
					}
				}
			}
		}
		Random rand = new Random();
		int i = rand.nextInt(playersCount); 
		return playersOnTile[i];
	}

	public int getRoadSize(Player p){
		/*
		Edge[] partOfLongRoad;
		CatanVertex[] vertexVisitedOnLongRoad;
		int length = 0;
		int longestRoadCount = 0;
		//return the length of player p's longest road
		for (int i = 0; i< vertices.length; i++){
			if (vertices[i].owner == p){
				Edges[] nextEdge = vertices[i].getEdges();
				while (nextEdge.length()>0){
					for(int j = 0; j<nextEdge.length; nextEdge++){
						if(j.owner = p){
							partOfLongRoad[length]= e;
						}
					}
				}
				CatanVertex[] oneAway = vertices[i].getAdjacentVs(vertices[i]);
				for (int i = 0; i<oneAway.length; i++){
					
				}
			}
		}
		*/
		return 3;
	}
	
	
	//go through the vetices array and fill in toReturn
	//toReturn[i][0] = settlementType, 1 = settlement 2 = city
	//toReturn[i][1] = owner player ID
	public int[][] getVerticesAndSettlementTypes (){
		int[][] toReturn = new int[vertices.length][2];
		for (int i = 0; i< vertices.length; i++){
			if (vertices[i].getOwner() != null){
				toReturn[i][0]= vertices[i].getSettlementType();
				toReturn[i][1] = vertices[i].getOwner().getID();
			}
		}
		return toReturn;
	}
	
	public int[] getResourceDots(){
		int[] dotsForResource = new int[6];
		for (int i=0; i<tiles.length; i++){
			int roll = tiles[i].roll;
			int resource = tiles[i].resource;
			if (roll == 2 || roll == 12){
				dotsForResource[resource] = dotsForResource[resource] + 1;
			} else if (roll == 3 || roll == 11){
				dotsForResource[resource] = dotsForResource[resource] + 2;
			} else if (roll == 4 || roll == 10){
				dotsForResource[resource] = dotsForResource[resource] + 3;
			} else if (roll == 5 || roll == 9){
				dotsForResource[resource] = dotsForResource[resource] + 4;
			} else if (roll == 6 || roll==8) {
				dotsForResource[resource] = dotsForResource[resource] + 5;
			}
		}
		return dotsForResource;
	} 
	
	public int getScarcestResource(){
		ResourceTranslator translator = new ResourceTranslator();
		int r = 1;
		int[] dots = getResourceDots();
		for (int i =1; i<dots.length; i++){
			if (debug){
				System.out.println("Resource type "+i+" has "+dots[i]+" dots");
			}			
			if (dots[i]<dots[r]){
				r = i;
			}
		}
		if (debug){
			System.out.println("Found the scarcest resource in this board. That resource is type: "+r);
		}
		return r;
	}
	
	public int getMostBountifulResource(){
		ResourceTranslator translator = new ResourceTranslator();
		int r = 1;
		int[] dots = getResourceDots();
		for (int i =1; i<dots.length; i++){
			if (debug){
				System.out.println("Resource type "+i+" has "+dots[i]+" dots");
			}
			if (dots[i]>dots[r]){
				r = i;
			}
		}
		if (debug){
			System.out.println("Found the most bountiful resource in this board. That resource is type: "+r);
		}
		return r;
	}
	
	
		
}
