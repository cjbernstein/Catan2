//JE +CJ

public class GameLogic {
	/*
	-Tasks:
	1.initialize the board
	2. Handle all logic for changing state of board/player hands
	*/
	public GraphController graph;
	private DevCardDeck devDeck;

	private boolean debugSet = false;
	private boolean debug = false;

	public Player[] players;

	public GameLogic(int[][] board, Player[] pArray) {
		GraphMaker gm = new GraphMaker(board);
		graph = new GraphController(gm.getVertexArray(), gm.tilesInBoard);
		graph.getScarcestResource();
		graph.getMostBountifulResource();
		devDeck = new DevCardDeck();
		players = pArray;
	}

	public void diceRoll(int numRoll){
		if(numRoll == 7){
			for(int i=1; i<players.length; i++)
				players[i].sevenRoll();
			//initiate robber movement stealing sequence (same as for knight)
		}
		else{
			graph.distributeResources(numRoll);
		}

	}
	
	public int[] getVerticesWithSettlements(int p){
		return players[p].getSettlementVertices();
	}

	
	public boolean placeSetCheck(int p, int vertexNumber){
		return graph.checkPlaceSettlement(vertexNumber, players[p], debugSet);
	}
	
	//method to be called at start of game. will not check that player has enough resources
	public boolean placeSettlement(int p, int vertexNumber){
		boolean build = placeSetCheck(p, vertexNumber);
		if (debugSet){
			System.out.println("Place settlement at "+vertexNumber+" " + build);
		}
		if (build == true){
			//update stats in player class
			players[p].placeSettlement(vertexNumber);
			graph.addSettlementToGraph(vertexNumber, players[p]);
			return true;
		}
		else{
			System.out.println("You cannot build on this location.");
			return false;
		}
	}
	
	//method to give player the resource for their second settlement
	public void giveResourcesStartGame(int vertexNumber){
		graph.firstRoundResource(vertexNumber);
	}
	
	public boolean buildSetCheck(int p, int vertexNumber){
		//check player resources and the graph
		boolean toReturn = players[p].buildSetCheck();
		if(toReturn == false)
			return false;
		return  graph.checkBuildSettlement(vertexNumber, players[p], debugSet);
	}
	
	//method to be called during game play when player wants to build settlement
	public boolean buildSettlement(int p, int vertexNumber){
		boolean build = buildSetCheck(p, vertexNumber);
		if (debugSet){
			System.out.println("Trying to build settlement at "+vertexNumber+" " + build);
		}

		if (build == true){
			//update stats in player class
			players[p].buildSettlement(vertexNumber);
			graph.addSettlementToGraph(vertexNumber, players[p]);
			return true;
		}

		else{
			System.out.println("You cannot build on this location.");
			return false;
		}

	}
	
	public boolean buildCityCheck(int p, int v){
		//check that the player has resources to build a city and has cities left
		if (players[p].buildCityCheck() == false)
			return false;

		return graph.checkBuildCity(v, players[p], debugSet);
		
	}
	
	public boolean buildCity(int p, int vertexNumber){
		boolean build = buildCityCheck(p, vertexNumber);
		
		if (build == false){
			System.out.println("You cannot build a city on this location.");
			return false;
		}

		else{
			players[p].buildCity();
			graph.addCityToGraph(vertexNumber, players[p]);
			return true;
		}

	}

	public void moveRobber(int destinationTile, int playerMovingRobber){
		Player playerToLoose = graph.moveRobber(destinationTile);
		int resourceGained = playerToLoose.stealResource();
		players[playerMovingRobber].addResource(resourceGained, 1);
	} 
	
	public boolean buildRoadCheck(int p, int v1, int v2){
		//check that the player has resources to build a road and has roads left
		if(players[p].buildRoadCheck() == false)
			return false;
		if (debug){
			System.out.println("You can build a road, now checking location on graph.");
		}
		return graph.checkBuildRoad(v1,v2, players[p], debugSet); 
	}
	
	public boolean buildRoad(int p, int v1, int v2){
		boolean build = buildRoadCheck(p, v1, v2);
		if (build == false){
			System.out.println("You cannot build a road on this location.");
			return false;
		} else{
			players[p].buildRoad();
			longRoadChecker(p);
			graph.addRoadToGraph(v1, v2, players[p]);
			return true;
		}
		//longest road check
	}
	
	public boolean legalRoadCheck(int p, int v1, int v2){
		return graph.checkPlaceRoad(v1,v2, players[p], debugSet); 
	}

	public Edge[] legalRoadsRound1(int p){
		return graph.getLegalRound1Roads(players[p]);
	}
	
	public boolean round1RoadCheck(int v1, int v2, int p){
		return graph.checkPlaceRound1Road(v1,v2, players[p], debugSet); 
	}

	//used at beginning
	public boolean placeRoad(int p, int v1, int v2){
		boolean build = graph.checkPlaceRound1Road(v1,v2, players[p], debugSet); 
		if (build == false){
			if (debug){
				System.out.println("You cannot build a road on this location.");
			}
			return false;
		}

		else{
			players[p].placeRoad();
			longRoadChecker(p);
			if (debug){
				System.out.println("Road placed successfully");
			}
			graph.addRoadToGraph(v1,v2,players[p]);
			return true;
		}
	}
	
	//this should be called after any instance of someone building a road (placeRoad and buildRoad)
		public void longRoadChecker(int p){
			int maxNumRoads = 0;
			int winningPlayer = 0;
			for (int i = 1; i< players.length; i++){
				if (players[p].getNumRoads() > maxNumRoads){
					maxNumRoads = players[p].getNumRoads();
					winningPlayer = p;
				}
			}
			if (maxNumRoads < 5){
				return;
			}
			if(debug){
				System.out.println("Max number of roads is greater than 5. Player with most roads is player"+winningPlayer);
			}
			if (players[winningPlayer].checkLongRoad() == true){
				//player with longest road already has earned those points
				return;
			} else {
				if(debug){
					System.out.println("Changing long road status for player"+winningPlayer);
				}
				players[winningPlayer].changeLongRoad();
				for (int i = 1; i< players.length; i++){
					if (players[i].checkLongRoad() == true && i != winningPlayer){
						players[i].changeLongRoad();
					}
				}
			}
			
			/*
			if(graph.getRoadSize(players[p])>=5 && players[p].checkLongRoad()==false){
					for(int m=0; m<players.length; m++){
						if(players[m].checkLongRoad() == true){
							if(graph.getRoadSize(players[p]) > graph.getRoadSize(players[m])){
								players[p].changeLongRoad();
								players[m].changeLongRoad();
							}
							break;
						}
					}
			}
			*/
		}
	
	public boolean buildDevCheck(int p ){
		boolean hasResources = players[p].buildDevCheck();
		if (hasResources == false){
			return false;
		}
		int i = devDeck.drawDevCheck();
		if(i == 10){
			if (debug){
				System.out.println("There are no development cards left.");
			}
			return false;
		}
		return true;
	}
	
	public boolean buildDevCard(int p){
		boolean build = buildDevCheck(p);
		if (build == false){
			return false;
		} 
		else {
			int i = devDeck.drawDevCard();
//			System.out.println("used dev resources");
			players[p].buildDev(i);
			return true;	
		}
	}

	//i is which dev card! 0 knight, 3 rb, 4 monopoly, 5 yop
	public boolean canUseDevCard(int p, int i){
		return players[p].canUseDevCard(i);
	}

	public boolean useDevCard(int playerID, int i){
		boolean canUse = canUseDevCard(playerID, i);
		if (canUse){
			if (i == 0){
				useKnight(playerID);
				return true;
			} 
			else {
				return players[playerID].useDevCard(i);
			}
		}
		return false;
	}
	
	public boolean useKnight(int p){
		//System.out.println("If this is printing, you played a knight so we need to check if you have largest army. You should automatically get your two points. Tell CJ if this isnt working");
		boolean canUse = canUseDevCard(p, 0);
		if (!canUse){
			System.out.println("Tried to use a knight, but player "+p+" does not have any knight cards");
			return false;
		}
		players[p].useDevCard(0);
		boolean truechecker = false;
		if(players[p].getArmySize()>=3 && players[p].checkLgArmy()==false){
			for(int m=1; m<players.length; m++){
				if(m!=p){
					if(players[m].checkLgArmy() == true){
						truechecker=true;
						if(players[p].getArmySize() > players[m].getArmySize()){
							players[m].changeLgArmy();
							players[p].changeLgArmy();
						}
						break;
					}
				}
			}
		if(truechecker==false)
			players[p].changeLgArmy();
		}
		return true;
	}
	
	public boolean useRoadBuilder(int p, int v1, int v2, int roadbuildcounter){
		boolean canUse = canUseDevCard(p, 3);
		if (!canUse){
			System.out.println("Tried to use a road builder, but player "+p+" does not have any");
			return false;
		}
		if(roadbuildcounter == 2)
			players[p].useDevCard(3);
		boolean build = graph.checkBuildRoad(v1,v2, players[p], debugSet);
		if (build == false){
			System.out.println("You cannot build a road on this location.");
			return false;
		} 
		else{
			players[p].placeRoad();
			longRoadChecker(p);
			graph.addRoadToGraph(v1, v2, players[p]);
			return true;
		}

	}
	
	public void useMonopoly(int p, int r){
		boolean canUse = canUseDevCard(p, 4);
		if (!canUse){
			System.out.println("Tried to use monopoly, but player "+p+" does not have one");
			return;
		}
		players[p].useDevCard(4);
		//r is the resource we are monopolizing
		int total = 0;
		//System.out.println("Okay, I think this is the problem with monopoly. What you should see here is # of players minus one statements (so if 3 players, 2 statements) that count up to the total number of the resource you are monopolizing. Then it should say total at end of loop. Please report back to CJ what happens.");
		for(int i=1; i<players.length; i++){
			if(i!=p){
				total = total + players[i].getAllX(r);
				System.out.println("total is now: "+total);
			}
		}
		System.out.println("total at end of loop = "+total);
		players[p].addResource(r,total);
	}

	public void useYearOfPlenty(int p, int r1, int r2){
		boolean canUse = canUseDevCard(p, 5);
		if (!canUse){
			System.out.println("Tried to use Year of Plenty, but player "+p+" does not have one");
			return;
		}
		players[p].useDevCard(5);
		players[p].addResource(r1, 1);
		players[p].addResource(r2, 1);
	}


	public boolean use3to1Port(int p, int x, int r, int y){
		//p is the player, x is which port they want to use, r is what resource they want, y is what resource they are using
		//see if i can edit this for 3:1 port
		boolean build = players[p].usePortCheck(x);
		if (build == false)
			return false;
		else{
			if(x==0)
				players[p].looseResource(y,3);
			else
				players[p].looseResource(y,2);
			players[p].addResource(r,1);
			return true;
		}
	}
	
	public boolean checkTrade(int[][] tradeStats){
		//tradeStats[0]= {type you want, amount, playerID to give}, tradeStats[1] = {type you'll give away, amount, playerID initiating trade}
		//check that person gaining has enough resources to give away
		boolean tradePossible = hasResourcesToTrade(tradeStats[1][2], tradeStats[1][0], tradeStats[1][1]);
		if (tradeStats[0][2]!=0) {
			//check that other person has enough resources to give away
			tradePossible = tradePossible && hasResourcesToTrade(tradeStats[0][2], tradeStats[0][0], tradeStats[0][1]);
		}
		return tradePossible;
	}
	
	public boolean hasResourcesToTrade(int p, int resourceType, int numToTrade){
		if (debug){
			System.out.println("Checking if trade is possible with player" + p );
		}
		int totalResourcesOfType = players[p].numResourcesOfType(resourceType);
		return totalResourcesOfType >= numToTrade;
	}
	
	public int[] getResourcesWantedInTrade(int p){
		int[] resourceTypes = new int[5];
		int typesCount = 0;
		int[] playerCurrentResources = players[p].getResourceArray();
		for (int i = 0; i<playerCurrentResources.length; i++){
			if (playerCurrentResources[i] == 0){
				resourceTypes[typesCount] = i;
				typesCount++;
			}
		}
		if (typesCount == resourceTypes.length){
			return resourceTypes;
		} else {
			int[] toReturn = new int[typesCount];
			for (int i = 0; i<toReturn.length; i++){
				toReturn[i] = resourceTypes[i];
			}
			return toReturn;
		}
	}
	

	public void trade(int[][] tradeStats){
		//tradeStats[0]= {type you want, amount, playerID to give}, tradeStats[1] = {type you'll give away, amount, playerID initiating trade}
		// if playerID to give is 0, the player initiating trade is trading with bank
		boolean tradePossilbe = checkTrade(tradeStats);
		if(tradePossilbe){
			if (tradeStats[0][2]!=0){ //not trading with computer
				Player a = players[tradeStats[0][2]];
				System.out.println("Player: "+a.getID() +" gaining: "+tradeStats[1][0]);
				//player a gives away resources and gains some
				a.looseResource(tradeStats[0][0], tradeStats[0][1]);
				a.addResource(tradeStats[1][0], tradeStats[1][1]);
			}
			Player b = players[tradeStats[1][2]];	
			//player b gives away resources and gains some
			if (debug){
				System.out.println("Player: "+b.getID()+ " gaining: "+tradeStats[0][0]);
			}
			b.addResource(tradeStats[0][0], tradeStats[0][1]);
			b.looseResource(tradeStats[1][0], tradeStats[1][1]);
		}
		else{
			System.out.println("Players do not have the appropriate resources.");
		}
	}
	
	public boolean checkUsePort (int player, int portType){
		boolean hasResource;
		if (portType != 0){
			//make sure player has at least 2 of required resource
			hasResource = hasResourcesToTrade(player, portType, 2);
			//make sure player is built on the correct port	
			if (!hasResource && debug){
				System.out.println("You don't have enough resources to use this port");
			}
		} else {
			hasResource = true;
		}
		boolean hasPort = players[player].usePortCheck(portType);
	
		if (!hasPort && debug){
			System.out.println("You are not built on this port");
		}
		return hasResource && hasPort;
	}
	
	//this is just for the 2:1 port. 3:1 port is handled with trade()
	public void usePort ( int playerID, int portType, int resourceDesired){
		boolean canUsePort = checkUsePort(playerID, portType);
		if (canUsePort){
			players[playerID].looseResource(portType, 2);
			players[playerID].addResource(resourceDesired, 1);
		}
	}
	
}
