import java.util.Random;

public class InitialPlacementAI {
	Random generator;
	RunGame rg;
	GameLogic gl;
	private DecisionTree dt;
	int playerWithDT;
	boolean fixedInitialPlacement;
	
	int[] settlementVertices;
	int settlementVerticesCount;
	int[][] roadVertices;
	int roadVerticesCount;
	boolean debug = false; 
	
	public InitialPlacementAI (RunGame r, GameLogic g, boolean printMessages, boolean f, int smartPlayer){
		generator =  new Random();
		rg = r;
		gl = g;
		fixedInitialPlacement = f;
		dt = new DecisionTree(g.graph);
		playerWithDT = smartPlayer;
	}
	
	// actions for first round (settlement and road placement) ///
		public int firstRoundPlaceSettlement(int player){
			int possibleSpot = 100;
			if (fixedInitialPlacement){
				///figure out some way to be systematic about the starting position for each player and make sure that all vertices are ttried at some point....
			}else {
				possibleSpot = spotToBuild(player);
				rg.placeSettlementFirstRound(possibleSpot);
			}
			return possibleSpot;
		}
		
		private int spotToBuild(int p){
			settlementVertices = new int[54];
			settlementVerticesCount = 0;
			for (int i = 0; i<settlementVertices.length; i++){
				if (gl.placeSetCheck(p, i)){
					settlementVertices[settlementVerticesCount] = i;
					settlementVerticesCount ++;
					if (debug){
						System.out.println("Possible to build a settlment on vertex: "+i+ " for player "+p);
					}
				}
			}
			if (p == playerWithDT){
				int[] newSettlementVertices = new int[54];
				int newSetVertCount = 0;
				for (int i = 0; i<settlementVertices.length; i++){
					if (dt.isSpotGood(settlementVertices[i])){
						newSettlementVertices[newSetVertCount] = settlementVertices[i];
						newSetVertCount ++;
					}
				}
				if (newSetVertCount >0){
					settlementVertices = newSettlementVertices;
					settlementVerticesCount = newSetVertCount;
				}
			}
			int randIndex = generator.nextInt(settlementVerticesCount);
			int vertexToBuild = settlementVertices[randIndex];
			return vertexToBuild;
		}
		
		public void firstRoundRoad(int p){
			findLegalRound1Road(p);
			if (roadVerticesCount == 0){
				return;
			}
			int randIndex = generator.nextInt(roadVerticesCount);
			int v1 = roadVertices[randIndex][0];
			int v2 =  roadVertices[randIndex][1];
			rg.placeRoadFirstRound(v1);
			rg.placeRoadFirstRound(v2);
		}
		
		private void findLegalRound1Road(int p){
			//only 6 possible roads to build from your first 2 settlements
			Edge[] edges = gl.legalRoadsRound1(p);
			roadVertices = new int[6][2];
			roadVerticesCount = 0;
			for (int i=0; i< edges.length; i++){
				int v1 = edges[i].v1.vertexNumber;
				int v2 = edges[i].v2.vertexNumber;
				if (gl.round1RoadCheck(v1,v2,p)){
					roadVertices[roadVerticesCount][0] = v1;
					roadVertices[roadVerticesCount][1] = v2;
					roadVerticesCount ++;
				}
			}
		}
}