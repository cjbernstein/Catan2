import java.io.*;

public class PlayManyGames {
	
	public static void main (String[] args){
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter (new FileWriter("FeatureData.txt"));
			writer.write("Data for the Play Many Games Class");
			writer.newLine();
		} catch (Exception e){
			System.out.println("Can't open the file: Feature Data.txt");
		}
		
		
		SpotQualityAlgorithm spotQuality = new SpotQualityAlgorithm ();
		int numRuns = 10;
		
		for (int j=0; j<numRuns; j++){
			RunGame gameRunner = new RunGame(4, false, true, true); //will used a fixed board
			int winningPlayer = gameRunner.runGameWithAI(false);
			GraphController theGraph = gameRunner.gl.graph;
			CatanVertex[] verticesInGraph = theGraph.vertices;
			Player[] players = gameRunner.getPlayers();
			int[][] initialSettlementsAllPlayers = gameRunner.getInitialPlayerSets();
			int worstPlayer = 0;
			int minVPs = 10;
			for (int i = 1; i<players.length; i++){
				if (i != winningPlayer){
					int vps = players[i].victoryPoints;
					if (vps< minVPs){
						minVPs = vps;
						worstPlayer = i;
					}
				}
			}
			
			
			for (int i = 1; i<initialSettlementsAllPlayers.length; i++){
				int [] playerStartSettlements = initialSettlementsAllPlayers[i];
				int spot1 = playerStartSettlements[1];
				int spot2 = playerStartSettlements[2];
				if (i == winningPlayer){
					saveDataAboutFeats(verticesInGraph[spot1], 1, writer);
					saveDataAboutFeats(verticesInGraph[spot2], 1, writer);
					/* learning
					// incriment feature weights for the winning player 
					spotQuality.incrementVertexNumWeight(spot1);
					spotQuality.incrementVertexNumWeight(spot2);
					*/
				}  else if (i == worstPlayer){
					saveDataAboutFeats(verticesInGraph[spot1], 0, writer);
					saveDataAboutFeats(verticesInGraph[spot2], 0, writer);
					/* learning
					//decriment feature weights for loosing players 
					spotQuality.decrementVertexNumWeight(spot1);
					spotQuality.decrementVertexNumWeight(spot2);
					*/
				}
			}
		}
		try{
			writer.close();
		} catch (Exception e){
			System.out.println("Can't close!");
		}
	}
		
	private static void getFeaturesWithGameState(CatanVertex v){
		//might require passing the entire board or information about player hand
	}
		
	private static void saveDataAboutFeats(CatanVertex v, int winLoss, BufferedWriter writer){
		GetSpotFeatures feats = new GetSpotFeatures();
		int[] features = feats.getFeaturesForVertex (v);
		
		//print stats in some meaningful way
		if (writer != null){
			try{
				writer.write("Feature information for vertex: "+v.vertexNumber);
				writer.newLine();
				for (int i = 0; i<features.length; i++){
					writer.write(" "+features[i]+",");
				}
				writer.write(" "+winLoss);
				writer.newLine();
				
			} catch (Exception e){
				System.out.println("OOPS");
			}
		}
		
	}
		
}