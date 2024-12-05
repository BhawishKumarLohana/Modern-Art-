import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a player in the ModernArt game
 * 
 * You are not allowed to add any new field to this class
 * You are not allowed to add any new public method to this class
 */
public class Player {
    /**
     * The name of the player
     * 
     * The first player should have the name "Player 0"
     * The second player should have the name "Player 1"
     * The third player should have the name "Player 2"
     * ...
     */
    private final String name;
    /**
     * The money the player has
     */
    private int money;
    /**
     * The total number of players in the game
     */
    private static int totalPlayers = 0;
    /**
     * The paintings the player has in hand
     */
    private List<Painting> handPaintings = new ArrayList<>();
    /**
     * The paintings the player has bought
     */
    private List<Painting> boughtPaintings = new ArrayList<>();
    /**
     * Constructor of the Player class
     */
    public Player(int money) {
        this.name="Player "+totalPlayers;
        this.money=money;
        totalPlayers++;
    }
    /**
     * To deal a painting to the player
     */
    public void dealPaintings(Painting painting) {
        painting.setOwner(this);
        handPaintings.add(painting);

    }
    /**
     * Get the name of the player
     */
    public String getName() {
        return this.name;
    }
    /**
     * To let the player to put up a painting for auction
     * After this method, the painting should be removed from handPaintings
     * 
     * Validation of user's input should be done in this method,
     * such as checking if the index is valid. If it is invalid,
     * the player will need to enter the index again.
     */
    public Painting playPainting() {

        boolean isOkay=false;
        int index = -1;

        // Validation Checking
        while(isOkay==false) {
            System.out.println(toString());
            for(int i = 0 ; i < handPaintings.size() ; i++){
                System.out.println(i+": "+handPaintings.get(i).toString());
            }
            try {
                Scanner in = new Scanner(System.in);
                System.out.print("Please enter the index of the Painting you want to play: ");
                index = in.nextInt();
                if(index < 0 || index > handPaintings.size()-1){
                    isOkay=false;
                }else{
                    isOkay=true;
                }
            }catch (Exception e){
                System.out.println("Invalid input. Please enter a valid integer.");
                isOkay=false;
            }
        }




        Painting Holder = handPaintings.get(index);
        handPaintings.remove(index);

        //System.out.println(this.getName() + " has "+this.getMoney());
        //System.out.println(handPaintings.get(index).getArtistName());
        return Holder;


    }
    /**
     * Get the money the player has
     */
    public int getMoney() {
        return money;
    }
    /**
     * To let the player to bid. 
     * 
     * In some auctions, e.g. open auction, the player knows the current bid.
     * In this case the currentBid will be passed to the method.
     * 
     * In some auctions, e.g. blind auction, the player does not know the current bid.
     * In this case, the currentBid passed to the method will be 0.
     * 
     * A human player should be asked to input the bid amount.
     * The bid amount should be less than or equal to the money the player has.
     * If the bid amount is too high, the player should be asked to input again.
     * 
     * If the bid amount is too small (less than the current bid or less than 1),
     * the bid amount will also be returned, which may means to pass the bid.
     * 
     * You should not assume there is only open auction when writing this method
     */
    public int bid(int currentBid) {
        int bidAmount = 0;
        Scanner in =   new Scanner(System.in);
        boolean isOkay=false;
        while(!isOkay){

            try{
                System.out.println(this.getName()+" has $"+this.getMoney());
                System.out.print("Enter your bid (enter 0 = forfeit): ");
                bidAmount = in.nextInt();
                isOkay=true;

                while(bidAmount>this.getMoney()){
                    System.out.println(this.getName()+" has $"+this.getMoney());
                    System.out.print("Enter your bid (enter 0 = forfeit): ");
                    bidAmount = in.nextInt();
                }

            }catch (Exception e){
                in.next(); // clear the buffer
                System.out.println();
                System.out.println("Invalid input. Please enter a valid integer");
                isOkay=false;
            }



        }



        return bidAmount;



    }
    /**
     * To let the player to pay
     */
    public void pay(int amount) {
        this.money = this.money-amount;
    }
    /**
     * To let the player to earn
     */
    public void earn(int amount) {
        this.money= this.money+amount;
    }
    /**
     * toString method that you need to override
     */
    public String toString() {
        return this.getName() +" has $"+this.getMoney();

    }
    /**
     * To finalize a bid and purchase a painting
     * 
     * This method has been finished for you
     */
    public void buyPainting(Painting Painting) {
        boughtPaintings.add(Painting);
    }
    /**
     * To sell all the paintings the player has bought to the bank 
     * after each round
     */
    // "0. Manuel Carvalho", "1. Sigrid Thaler", "2. Daniel Melim", "3. Ramon Martins", "4. Rafael Silveira
    //       20               	10                  	0	              30             	0
    public void sellPainting(int[] scores) {
        for (Painting Eachpainting : boughtPaintings) {
            int score = scores[Eachpainting.getArtistId()];
            earn(score);
        }
        boughtPaintings.clear();
    }
}
