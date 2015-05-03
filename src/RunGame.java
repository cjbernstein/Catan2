import java.util.*;
/*
 * The class that will keep track of what's going on in the game
 * 1. Ask for number of players
 * 2. Create GP and have it initialize board
 * 3. Create GUI/build board
 * 4. Create front end interface
 * 
 */
public class RunGame {
	private static Scanner sc = new Scanner(System.in);
	private  boolean usingGraphics;
	TurnOrderManager order;
	public  GameLogic gl;
	private  FrontEndInterface fei;
	int endGameCondition = 7;
	public int[][] board;
	
	private  Player[] players;
	private  int playerCount;
	private  int currentPlayerID;
	
	private  int actionType; //0= nothing, 1 = settlement, 2 = city, 3 = road,
	//4 = trade 4 to 1, 5 = trade other player, 6 = move robber, 7 = monopoly 8 = year of plenty
	//9 = road builder, 10 = knight, 11 = buy dev card, 12 = use 3 to 1 port, 13 = rock port, 14 = Wheat port, 15 = brick port, 
	//16 = wood port, 17 = Sheep port
	private  int[] verticesToAct; //at most 2 vertices
	private int vertexCounter;
	private boolean sevenRolled;
	private int[][] tradeResources; //tradeResources[0]= {type you want, amount, playerID}, tradeResouces[1] = {type you'll give away, amount, playerID}
	private int[] yopResources;
	private int roadBuilderCounter; //for road builder dev card

	public boolean inFirstRound;
	private boolean firstRoundSET;
	private int firstRoundRoadCounter;

	private int turnMax = 250;
	
	int[][] initialSettlementsForPlayers;
	
	
	boolean AI;//how to mimic one player as AI
	TurnAI np;
	private boolean printRunningMessage = true;
	
	public RunGame(int numPlayers, boolean useGraphics, boolean ai, boolean testBoard){
		AI = ai;
		System.out.println("AI "+AI);
		players = new Player[numPlayers+1];
		
		for(int i=1; i<(numPlayers+1); i++)
			players[i] = new Player(i);
		this.playerCount = numPlayers;
		order = new TurnOrderManager (numPlayers);
		currentPlayerID = order.getCurrentPlayer();
		verticesToAct = new int[2];
		vertexCounter = 0;
		tradeResources = new int[2][3];
		yopResources = new int[2];
		
		usingGraphics = useGraphics;
		//testboard gives a predetermined board
		board= new Board().getBoard();
		if (testBoard){
			board = new Board().getTestBoard();
		}
		//pass this to gl 
		gl = new GameLogic(board, players);
		int[] ports = new int[9];
		for (int i=0; i< ports.length; i++){
			ports[i] = board[2][i];
		}
	
		inFirstRound = true;
		firstRoundSET = true;
		if (usingGraphics){
			//FEI will draw the graph
			fei = new FrontEndInterface (this, board, numPlayers, ports, true);
			fei.currentPlayerID = currentPlayerID;
			if (AI){
				np = new TurnAI(this, gl, printRunningMessage);
			}
		} 
	}
	
	//returns player ID of winning player
	public int runGameWithAI(boolean print){
		int numTurns = 0;
		printRunningMessage = print;
		// run the game without graphics and with a random AI making all choices
		InitialPlacementAI initial = new InitialPlacementAI(this, gl, print, false /*don't set the initial spots */);
		TurnAI turn = new TurnAI(this, gl, print);
		int settlementPosition;
		initialSettlementsForPlayers = new int[5][3]; //each player stores {playerID, spot1, spot2}
		//do something to store start settlements and winners
		for (int i=0; i<playerCount*2; i++){
			int playerPlacingSettlement = currentPlayerID;
			settlementPosition = initial.firstRoundPlaceSettlement(playerPlacingSettlement);
			if (i< playerCount){
				//System.out.println("i "+i);
				//first time you have placed a settlemnt for this player, store player id as first element of list
				initialSettlementsForPlayers[playerPlacingSettlement][0]= playerPlacingSettlement;
				initialSettlementsForPlayers[playerPlacingSettlement][1]= settlementPosition;
			} else {
				//System.out.println("in else "+i);
				initialSettlementsForPlayers[playerPlacingSettlement][2]= settlementPosition;
			}
		}
		for (int i=0; i<playerCount*2; i++){
			initial.firstRoundRoad(currentPlayerID);
		}
		while(!gameEnd(false)){
			numTurns ++;
			int[] r = rollDice();
			turn.turn(currentPlayerID);
			if(numTurns>turnMax){
				gameEnd(true);
				return -1;
			}
		}
		int winningPlayer = 0;
		for (int i = 1; i< players.length; i++){
			int vp = players[i].victoryPoints;
			System.out.println("Player: "+i+" ended with vps "+vp);
			if (vp>= endGameCondition){
				winningPlayer = i;
			}
		}
		System.out.println("first Settlement for winner("+winningPlayer+" was "+initialSettlementsForPlayers[winningPlayer][1]+ ") and second was "+initialSettlementsForPlayers[winningPlayer][2]);
		System.out.println("numTurns: "+numTurns);
		
		return winningPlayer;
	}
	private int roll(){
		//pick a random int between 1 and 6
		Random generator =  new Random();
		int roll = generator.nextInt(6);
		return roll+1;
	}
	
	public int[] rollDice(){
		if ( gameEnd(false) ){
			//System.exit(0);
			return new int[] {6,6};
		}
		currentPlayerID = order.getNextPlayer(printRunningMessage);
		if (usingGraphics){
			fei.updateCurrentPlayer(currentPlayerID);
		}
		int r1 = roll();
		int r2 = roll();
		if ((r1+r2) == 7){
			sevenRolled = true; 
		}
		gl.diceRoll(r1+r2);
		updateAllStats();
		return new int[] {r1,r2};
	}
	
	public void vertexClickedFirstRound( int vertex){
		if (firstRoundSET){
			placeSettlementFirstRound(vertex);
		} else {
			placeRoadFirstRound(vertex);
		}
	}
	
	public boolean placeSettlementFirstRound (int vertex){
		//settlement building part of first round
		if (printRunningMessage){
			System.out.println("vertex: "+vertex+" clicked in first round. Trying to place settlement for player: "+currentPlayerID);
		}
		if (gl.placeSettlement(currentPlayerID, vertex)){
			if (usingGraphics){
				fei.drawSettlement(vertex);
			}
			if(players[currentPlayerID].numberOfSettlements == 1){
				gl.giveResourcesStartGame(vertex);
				updateAllStats();
			}	
			if (!order.firstRoundSettlementDone()){
				currentPlayerID = order.getNextPlayerGameStart();// switch players
				if (usingGraphics){
					fei.updateCurrentPlayer(currentPlayerID);
				}
			} else {
				firstRoundSET = false;
				firstRoundRoadCounter = 0;
				if (printRunningMessage){
					System.out.println("Click vertexes for Roads");
				}
			}
			return true;
		}
		else{
			//was not able to place a settlement for some reason
			return false;
		}
	}
	
	public boolean placeRoadFirstRound(int vertex){
		//road placement part of Round 1
		if (printRunningMessage){
			System.out.println("vertex: "+vertex+" clicked in first round. Trying to place road for player: "+currentPlayerID);
		}
		verticesToAct[vertexCounter] = vertex;
		vertexCounter ++;
		boolean toReturn = false;
		if (vertexCounter == 2){
			if (gl.placeRoad(currentPlayerID, verticesToAct[0], verticesToAct[1])){
				if (usingGraphics){
					fei.drawRoad(verticesToAct[0], verticesToAct[1]);
				}
				currentPlayerID = order.getNextPlayer(printRunningMessage);
				firstRoundRoadCounter ++;
				if (usingGraphics){
					fei.updateCurrentPlayer(currentPlayerID); //switch players
				}
				
				toReturn = true;
			} else{
				System.out.println("Road placement didn't work, try again");
			}
			vertexCounter = 0;
			if (firstRoundRoadCounter == 2*playerCount){
				inFirstRound = false;
				if (printRunningMessage){
					System.out.println("Initial Settlement and Road Placement is done");
				}
			}
		}
		return toReturn;
	}
	
	public void setTileClicked(int t){
		if (/*actionType == 6 &&*/ sevenRolled) {
			robberAction(t, currentPlayerID);
		} if (actionType == 10){
			useKnight(t);
		}
	}
	
	public void useKnight(int tile){
		//p is the player number which we need as input - I will leave that to you since you've been doing it
		boolean allowed = gl.useKnight(currentPlayerID);
		if(allowed){
			//System.out.println("You are allowed to use a knight, time to use it.");
			robberAction(tile, currentPlayerID);
		}
		clearVerticesAndAction();
		updateAllStats();
		//System.out.println("Working?");
	}

	public void robberAction(int tile, int playerID){
		gl.moveRobber(tile, playerID);
		fei.updateRobberPosition(tile);
		updateAllStats();
	}
	
	public void setActionType (int t){
		if (t == 4 ){
			System.out.println("Trade started. Click num of resources you want, then the num of resources you'll give up.");
		/*	int[] rs = gl.getResourcesWantedInTrade(currentPlayerID);
			System.out.println("Resource array length"+rs.length);
			for (int i = 0; i<rs.length; i++){
				System.out.println("resource"+rs[i]);
			}
			*/
		}
		if (actionType == 4 && t == 4){//player already clicked the trade button once
			tradeResourceButton();
		} else if (t == 8){
			yopResources = new int[2];
		} else if (t == 11){
			buyDevCard();
		} else if (t == 12){
			System.out.println("3 to 1 port clicked. Click num of resources you want, then the num of resources you'll give up.");
		}
		actionType = t;
	}
	
	public void setVertex (int v){
		if (vertexCounter >= 2){
			System.out.println("You have clicked too many vertices. Clearing action and vertices.");
			clearVerticesAndAction();
		}
		verticesToAct[vertexCounter] = v;
		vertexCounter ++;
		if (actionType == 1){
			tryToBuildSettlement();
		} else if (actionType == 2){
			tryToBuildCity();
		} else if (actionType == 3){
			tryToBuildRoad();		
		} else if (actionType == 9){
			useRoadBuilder();
		} 
		if(actionType!=9)
			updateAllStats();
		//why? because we don't want to get rid of road builder yet. 		
	}
	
	public void buyDevCard(){
		gl.buildDevCard(currentPlayerID);
		updateAllStats();
	}
	
	private void tryToBuildSettlement(){
		int v = verticesToAct[0];
		//TEST:
		//players[currentPlayerID].giveSettlementResources();
		boolean success = 	gl.buildSettlement(currentPlayerID, v);
		if (success && usingGraphics){
			fei.drawSettlement(v);
		}
		clearVerticesAndAction();
	}
	
	private void tryToBuildCity(){
		int v = verticesToAct[0];
		//TEST:
		//players[currentPlayerID].giveCityResources();
		boolean success = 	gl.buildCity(currentPlayerID, v);
		if (success && usingGraphics){
			fei.drawCity(v);
		}
		clearVerticesAndAction();
	}
	
	private void tryToBuildRoad(){
		if (! (vertexCounter == 2)){
			return;
		} else {
			//TEST:
			//players[currentPlayerID].giveRoadResources();
			boolean success = gl.buildRoad(currentPlayerID, verticesToAct[0], verticesToAct[1]);
			if (success && usingGraphics){
				fei.drawRoad(verticesToAct[0], verticesToAct[1]);
			}
			clearVerticesAndAction();
		}
	}
	
	public void playerClicked(int playerID){
		if (actionType == 4){
			actionType = 5;
			tradeResources[0][2] = playerID; //want to trade with the player clicked
			tradeResources[1][2] = currentPlayerID;
		} 
		if (actionType == 5){
			if (printRunningMessage){
				System.out.println("Player "+tradeResources[1][2]+" is trading resource of type: "+tradeResources[0][0]+" " +
						"to player "+tradeResources[0][2]+" for resource of type: "+tradeResources[1][0]);
			}
			trade();
		}
	}
	
	public void tradeResourceButton(){
		if (printRunningMessage){
			System.out.println("Trade with bank");
		}
		if (actionType == 4 || actionType == 12){ //clicked trade but did not click player
			//4 to one trade
			tradeResources[0][2] = 0;//trading with computer
			tradeResources[1][2] = currentPlayerID;
		}
		trade();
	}
	
	//will be used by AI
	public void trade(int[][] completeTradeArray){
		if (printRunningMessage){
			System.out.println("Calling trade with AI");
		}
		//both elements have been filled in, pass to game logic 
		gl.trade(completeTradeArray);
		//reset the trade array;
		tradeResources = new int[2][3];
		updateAllStats();
	}
	
	public void trade(){
		if (printRunningMessage){
			System.out.println("Calling trade in game logic with type  "+actionType);
		}
		//both elements have been filled in, pass to game logic 
		gl.trade(tradeResources);
		//reset the trade array;
		tradeResources = new int[2][3];
		actionType = 0;
		updateAllStats();
	}
	
	//fills the array with type of resources and quantity
	public void resourceClicked( int resourceType ) {
		if (printRunningMessage){
			System.out.println("Resource Clicked");
		}
			if (actionType == 5 || actionType == 4 || actionType == 12){
				if (tradeResources[0][0] == 0){
					//nothing has been asked for
					if (printRunningMessage){
						System.out.println("setting resource wanted");
					}
					tradeResources[0][0] = resourceType;
					tradeResources[0][1] = 1;
				} else if (tradeResources[0][0] == resourceType) {
					if (printRunningMessage){
						System.out.println("incrimenting resource wanted");
					}
					tradeResources[0][1] ++;
				} else if (tradeResources[1][0] == 0 && tradeResources[0][0] != 0) {
					if (printRunningMessage){
						System.out.println("setting resource to give up");
					}
					tradeResources[1][0] = resourceType; 
					tradeResources[1][1] = 1;
					if (actionType == 12){
						//3 for 1 trade
						tradeResources[1][1] = 3;
						tradeResourceButton();
					}
				} else if (tradeResources[1][0]== resourceType) {
					if (printRunningMessage){
						System.out.println("Incrimenting resource to give up");
					}
					tradeResources[1][1]++;
				} 
			} else if (actionType == 7){
				useMonopoly(resourceType);
			} else if (actionType == 8){
				if (yopResources[0] == 0){
					yopResources[0] = resourceType;
				} else {
					yopResources[1] = resourceType;
					useYearOfPlenty();
				}
			} else if (actionType == 13) {
				gl.usePort(currentPlayerID, 1, resourceType);
			} else if (actionType == 14) {
				gl.usePort(currentPlayerID, 2, resourceType);
			} else if (actionType == 15) {
				gl.usePort(currentPlayerID, 3, resourceType);
			} else if (actionType == 16) {
				gl.usePort(currentPlayerID, 4, resourceType);
			} else if (actionType == 17) {
				gl.usePort(currentPlayerID, 5, resourceType);
			}
			updateAllStats();
	}
	
	//dev card methods
	public void useMonopoly(int resource){
		//p is the player number which we need as input - I will leave that to you since you've been doing it
		//r is the resource (int) they want to monopolize from all the players
		boolean allowed = gl.canUseDevCard(currentPlayerID, 4);
		if(allowed){
			System.out.println("Monopoly use is allowed. Click the resource you want to monopolize.");
			gl.useMonopoly(currentPlayerID, resource);
		}
		clearVerticesAndAction();
		updateAllStats();
	}
	
	public void useYearOfPlenty(){
		//p is the player number which we need as input - I will leave that to you since you've been doing it
		//r1 and r2 are the two resources (int) they want to take from the bank
		boolean allowed = gl.canUseDevCard(currentPlayerID,5);
		if(allowed){
			System.out.println("You have the card, lets use it!");
			gl.useYearOfPlenty(currentPlayerID, yopResources[0], yopResources[1]);
		}
		else
			System.out.println("You do not have a year of plenty card.");
		clearVerticesAndAction();
		updateAllStats();
	}

	public void useRoadBuilder(){
		//p is the player number which we need as input - I will leave that to you since you've been doing it
		if (! (vertexCounter == 2)){
			return;
		} 
		else{
			boolean allowed = gl.canUseDevCard(currentPlayerID,3);
			if(allowed){
				//players[currentPlayerID].giveRoadResources(); //dont need this anymore because new methods in gl
					boolean success = gl.useRoadBuilder(currentPlayerID, verticesToAct[0], verticesToAct[1], roadBuilderCounter+1); 
					if (success){
						fei.drawRoad(verticesToAct[0], verticesToAct[1]);
						roadBuilderCounter++;
						verticesToAct[0] = 0;
						verticesToAct[1] = 0;
						vertexCounter = 0;
					}
				if(roadBuilderCounter==2){
					roadBuilderCounter=0;
					updateAllStats();
					clearVerticesAndAction();
				}
			}
		}
	}

	
	private void updateSinglePlayerStats(){
		int [] r = players[currentPlayerID].getPlayerStats();
		fei.updateVP(currentPlayerID, r);
	}
	
	private void updateAllStats(){
		if (!usingGraphics){
			return;
		}
		for (int i = 1; i< players.length; i++){
			int [] r = players[i].getPlayerStats();
			fei.updateVP(i, r);
		}
	}
	
	public void clearVerticesAndAction(){
		verticesToAct[0] = 0;
		verticesToAct[1] = 0;
		yopResources[0] = 0;
		yopResources[1] = 0;
		vertexCounter = 0;
		actionType = 0;
		tradeResources = new int[2][3];
		roadBuilderCounter = 0;
	}
	
	private boolean gameEnd(boolean turnMaxEnder){
		if(turnMaxEnder)
			return false;
		for (int i = 1; i< players.length; i++){
			int vp = players[i].victoryPoints;
			if (vp>= endGameCondition){
				System.out.println("GAME OVER. Winner is Player " + i);
				if (printRunningMessage){
					players[i].printStats();
				}
				return true;
			}
		}
		return false;
	}


	
	public Player[] getPlayers(){
		return players;
	}
	
	public int[][] getInitialPlayerSets(){
		return initialSettlementsForPlayers;
	}
	
}

