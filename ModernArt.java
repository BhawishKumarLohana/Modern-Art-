import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the ModernArt game and control the main logic of the game
 * 
 * You are not allowed to add any new field to this class
 * You are not allowed to add any new public method to this class
 */
public class ModernArt {

    /**
     * PRE_DEAL contains the number of paintings that should be dealt to each player before each round
     * 
     * So for example, PRE_DEAL[3] means for 3 players, the number of painting to be dealt to each player
     * before round 1 is 10, round 2 is 6, and round 3 is 6, and round 4 is 0
     */
    public static final int[][] PRE_DEAL = {null, null, null,  //game can't be played for 0, 1, 2 players
                                         {10,6,6,0}, {9,4,4,0}, {8,3,3,0}};
    /**
     * The game has 4 rounds in total, they are
     * 
     * Round 0, Round 1, Round 2, Round 3
     */
    public static final int ROUND = 4;
    /**
     * The initial money each player has is 100
     */
    public static final int INITIAL_MONEY = 100;
    /**
     * The number of paintings for each artist is fixed
     * "0. Manuel Carvalho" = 12 , 
     * "1. Sigrid Thaler" = 15, 
     * "2. Daniel Melim" = 15, 
     * "3. Ramon Martins" = 15, 
     * "4. Rafael Silveira" = 20
     */
    public static final int[] INITIAL_COUNT = {12,15,15,15,20};                             
    /**
     * The price of the most sold paintings is 30, 
     * the second most sold is 20, 
     * and the third most sold is 10
     * 
     * Tie-breaker: if two artists have the same number of painting sold
     * the one with the lower id will be the winner, i.e.,
     * 
     * If 0. Manuel Carvalho and 1. Sigrid Thaler have the same number of paintings sold
     * then 0. Manuel Carvalho will be considered have more paintings sold than 1. Sigrid Thaler
     * 
     */
    private static final int SCORES[] = {30, 20, 10};
    /**
     * Each round a painting can only be played for 5 times.
     * The 5th time the painting is played, it will not be placed in auction
     * and that round ends immediately
     */
    private static final int MAX_PAINTINGS = 5;
    /**
     * The number of players in the game, it should be between 3-5
     */
    private int noOfPlayers;
    /**
     * The array of players in the game
     */
    private Player[] players;
    /**
     * The deck of paintings
     */
    private List<Painting> deck = new ArrayList<>();
    /**
     * The score board of the game
     */
    private int[][] scoreboard = new int[ROUND][Painting.ARTIST_NAMES.length];

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                int noOfPlayers = Integer.parseInt(args[0]);
                if (noOfPlayers < 3 || noOfPlayers > 5) {
                    throw new Exception();
                }
                new ModernArt(noOfPlayers).startgame();
            } catch (Exception e) {
                System.out.println("Invalid argument. Please enter a valid integer between 3-5.");
            }
        } else
            new ModernArt().startgame();
    }
    public ModernArt(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
        this.players = new Player[noOfPlayers];
        for (int i = 0; i < noOfPlayers; i++) {
            players[i] = new Player(INITIAL_MONEY);
        }
        prepareDeck();
    }

    /**
     * Default constructor, the game will be played with 3 players by default
     */
    public ModernArt() {
        this.noOfPlayers =  3;
        this.players= new Player[noOfPlayers];
        for(int i = 0 ; i < noOfPlayers;i++){
            players[i]=new Player(INITIAL_MONEY);
        }
        prepareDeck();
    }
    /**
     * Prepare the deck of paintings
     */
    public void prepareDeck() {
        // 70 Cards are there
        for(int i = 0 ; i < INITIAL_COUNT.length;i++){
            for(int j = 0; j < INITIAL_COUNT[i] ;j++){
                deck.add(new Painting(i));
            }
        }
        shuffle(deck);
    }
    /**
     * Deal the paintings to the players. After this method,
     * each player should receive some number of new paintings in their hand
     * as specified in the PRE_DEAL array
     * 
     * The parameter round indicate which round the game is currently in
     */
    public void dealPainting(int round) {

        // Numbers of Players
        // Round
        // PREDEAL [ NumberofPlayers ][Round]  --  > 6
        // Assign 6 Cards from the deck to each of  our Numberofplayers

        int numofCards = PRE_DEAL[noOfPlayers][round];
        int  cardGivenCounter = 0;
        if(round > 0) {
            int sumPreviousCards= 0;
            for(int i = 1  ; i <= round; i++){
                sumPreviousCards=(PRE_DEAL[noOfPlayers][round - i] * noOfPlayers) +  sumPreviousCards;
            }
             cardGivenCounter = sumPreviousCards;
        }

        for(int i = 0 ; i < noOfPlayers ; i++){
            for(int j = 0 ; j < numofCards; j++){

                // given each player the specified number of cards
                // the cards are already shuffled

                players[i].dealPaintings(deck.get(cardGivenCounter));
                //System.out.println("CardGivenCounter: "+cardGivenCounter+"Player" +i +" assigned card "+ deck.get(cardGivenCounter).toString() );
                //deck.get(cardGivenCounter).setOwner(players[i]);
                //System.out.println("Player" +i +" assigned card "+ deck.get(cardGivenCounter).toString() );
                cardGivenCounter++;


            }


        }


    }

    /**
     * This method will update the score board after each round.
     * The score board updating rules please refer to the game description
     * 
     * The method also returns the price for each artist's painting in this round
     * 
     * The parameter round indicate which round the game is currently in
     * The parameter paintingCount indicates how many paintings each artist has sold in this round
     */
    public int[] updateScoreboard(int round, int[] paintingCount) {
        int[] UpdatedScoreboard  = new  int[paintingCount.length];
        int Counter = 0 ;
        int FirstHighestIndex = -1;
        int FirstHighest = -1;
        int SecondHighestIndex = -1;
        int SecondHighest = -1;
        int ThirdHighestIndex = -1;
        int ThirdHighest = -1;
        int FourthHighestIndex = -1;
        int FourthHighest = -1;
        int FifthHighestIndex = -1;
        int FifthHighest = -1;

        // We are going to search through our painting count to get the 5 highest with their  respective Indexes
        for (int i = 0; i < paintingCount.length; i++) {
            if (paintingCount[i] > FirstHighest) {
                FifthHighest = FourthHighest;FifthHighestIndex = FourthHighestIndex;

                FourthHighest = ThirdHighest;FourthHighestIndex = ThirdHighestIndex;

                ThirdHighest = SecondHighest;ThirdHighestIndex = SecondHighestIndex;

                SecondHighest = FirstHighest;SecondHighestIndex = FirstHighestIndex;

                // Updating the first Highest one first
                FirstHighest = paintingCount[i];FirstHighestIndex = i;
                //System.out.println("First Highest: "+FirstHighest);
            } else if (paintingCount[i] > SecondHighest && i != FirstHighestIndex) {
                FifthHighest = FourthHighest;FifthHighestIndex = FourthHighestIndex;

                FourthHighest = ThirdHighest;FourthHighestIndex = ThirdHighestIndex;

                ThirdHighest = SecondHighest;ThirdHighestIndex = SecondHighestIndex;

                SecondHighest = paintingCount[i];SecondHighestIndex = i;
            } else if (paintingCount[i] > ThirdHighest && i != FirstHighestIndex && i != SecondHighestIndex) {
                FifthHighest = FourthHighest;FifthHighestIndex = FourthHighestIndex;

                FourthHighest = ThirdHighest;FourthHighestIndex = ThirdHighestIndex;

                ThirdHighest = paintingCount[i];ThirdHighestIndex = i;
            } else if (paintingCount[i] > FourthHighest && i != FirstHighestIndex && i != SecondHighestIndex && i != ThirdHighestIndex) {
                FifthHighest = FourthHighest;FifthHighestIndex = FourthHighestIndex;

                FourthHighest = paintingCount[i];FourthHighestIndex = i;
            } else if (paintingCount[i] > FifthHighest && i != FirstHighestIndex && i != SecondHighestIndex && i != ThirdHighestIndex && i != FourthHighestIndex) {
                FifthHighest = paintingCount[i];FifthHighestIndex = i;
            }
        }
        int[][] ScoreboardRep = new int[5][5];  // 4 Rows (RONDS)  but 1 extra for total and 5 cols for each of the painters

        // Iteration through entire  thing while assigning the scoreboard with the values
        // Manual Assignment to Understand how each and every value is stored
        for(int i = 0 ; i < ScoreboardRep.length ; i++){
            for(int j = 0 ; j < ScoreboardRep[i].length;j++){
                if(i==round && j==FirstHighestIndex){
                    if(FirstHighest!=0) {
                        ScoreboardRep[i][j] = SCORES[0];
                    }else{
                        ScoreboardRep[i][j] = 0;
                    }
                }else if(i==round && j==SecondHighestIndex){
                    if(SecondHighest!=0) {
                        ScoreboardRep[i][j] = SCORES[1];
                    }else{
                        ScoreboardRep[i][j]=0;
                    }
                }else if(i==round && j==ThirdHighestIndex){
                    if(ThirdHighest!=0) {
                        ScoreboardRep[i][j] = SCORES[2];
                    }else{
                        ScoreboardRep[i][j]=0;
                    }
                }else if(i==round && j==FourthHighestIndex) {ScoreboardRep[i][j] = 0;
                }else if(i==round && j==FifthHighestIndex) {ScoreboardRep[i][j] = 0;
                }else if(i==round){ScoreboardRep[i][j]=-1;}

            }
        }
        // for each Artist traverse through their column
        // if value is 0 then start the summing again
        // Keep traversing till the end and summing it up



        for(int i = 0 ; i < ScoreboardRep.length-1;i++){
            for(int j =  0 ; j < ScoreboardRep[i].length;j++){
                if(round == i ) {
                    scoreboard[i][j] = ScoreboardRep[i][j];
                }
            }
        }

        for(int i  = 0 ; i < scoreboard.length;i++){
            for(int j = 0 ; j < scoreboard[i].length ; j++){
                System.out.print(" "+scoreboard[i][j]+" ");
            }
            System.out.println();
        }
        // Updated Scoredboard
        int sum  = 0;
        for(int i = 0 ; i <= round ; i++){
            for(int j = 0 ; j < scoreboard[i].length; j++){
                if(scoreboard[round][j]==0){
                    UpdatedScoreboard[j]=0;
                }else {
                    UpdatedScoreboard[j] += scoreboard[i][j];
                }
            }

        }



        /*
        int sum=0;

        for(int j = 0 ; j < scoreboard.length + 1; j++){
            for(int i = 0 ; i <= round; i++){
                if(scoreboard[i][j] ==0){
                    sum=0;

                }else{
                    sum=sum+scoreboard[i][j];
                }
            }
            UpdatedScoreboard[j]=sum;
            sum=0;

        }

         */


        return UpdatedScoreboard;

    }
    /**
     * This is the main logic of the game and has been completed for you
     * You are not supposed to change this method
     */
    public void startgame() {
        int currentPlayer = 0;
        
        for (int round = 0; round < ROUND; round++) {
            //deal the paintings
            dealPainting(round);
            //start auction
            int[] paintingCount = new int[Painting.ARTIST_NAMES.length];
            while (true) {
                Player player = players[currentPlayer];
                Painting p = player.playPainting();
                if (++paintingCount[p.getArtistId()] == MAX_PAINTINGS)
                    break; //this round end immediately and the painting is not putting up for auction

                if (p != null) {
                    p.auction(players); // Needs Validation Fixing but does work for now
                }
                currentPlayer = (currentPlayer + 1) % noOfPlayers;
                System.out.println("The number of painting sold: ");
                for (int i = 0; i < Painting.ARTIST_NAMES.length; i++) {
                    System.out.println(Painting.ARTIST_NAMES[i] + " " + paintingCount[i]);
                }
            }
            System.out.println("Complete auction - sell paintings");
            //update score board
            int[] scoreForThisRound = updateScoreboard(round, paintingCount);
            System.out.println("Print the score board after auction");
            System.out.print("\t\t");
            for (int j = 0; j < Painting.ARTIST_NAMES.length; j++) {
                System.out.print( "\t" + j);
            }
            for (int i = 0; i < round + 1; i++) {
                System.out.print("\nRound " + i + ":\t\t");
                
                for (int j = 0; j < Painting.ARTIST_NAMES.length; j++) {
                    System.out.print(scoreboard[i][j] + "\t");
                }
            }
            
            System.out.println("\n\nPrint the price for each artist's painting");
            for (int i = 0; i < Painting.ARTIST_NAMES.length; i++) {
                System.out.println(Painting.ARTIST_NAMES[i] + " " + scoreForThisRound[i]);
            }



            //Sell the paintings
            for (Player p : players) {
                p.sellPainting(scoreForThisRound);
            }
        }
        System.out.println("Game over, score of each player");
        for (int i = 0; i < noOfPlayers; i++) {
            System.out.println(players[i]);
        }
    }
    /**
     * Shuffle the deck of paintings
     * 
     * This method is completed for you.
     */
    public void shuffle(List<Painting> deck) {
        for (int i = 0; i < deck.size(); i++) {
            int index = ThreadLocalRandom.current().nextInt(deck.size());
            Painting temp = deck.get(i);
            deck.set(i, deck.get(index));
            deck.set(index, temp);
        }
    }
    
    /**
     * This method is completed for you. We use this for grading purpose
     */
    public int[][] getScoreboard() {
        return scoreboard;
    }
}
