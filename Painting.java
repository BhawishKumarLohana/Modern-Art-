import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

/**
 * The Painting class represents a painting in the game.
 * A painting has an artist, an owner, a current bidder and a current bid.
 * 
 * Each painting has a type of auction. In this assignment, all paintings
 * have the same type of auction, which is "Open Auction".
 * 
 * You are not allowed to add any new field to this class
 * You are not allowed to add any new public method to this class
 */
public class Painting {
    /**
     * The artist ID of the painting, should be between 0 and 4.
     */
    private final int artist_id;
    /**
     * The type of auction of the painting
     */
    private final String TYPE = "Open Auction";
    /**
     * The owner of the painting.
     * 
     * When the painting is dealt to a player, the owner is set to that player.
     * When the painting is sold in the auction, the owner is set to the player 
     * that won the auction.
     * After the painting is sold to the bank after each round, the owner 
     * information is irrelevant and can be set as any value.
     * 
     */
    private Player owner;
    /**
     * The current bidder of the painting.
     */
    private Player currentBidder;
    /**
     * The current bid of the painting.
     */
    private int currentBid;
    /**
     * The names of the artists
     */
    public static final String[] ARTIST_NAMES = {"0. Manuel Carvalho", "1. Sigrid Thaler", "2. Daniel Melim", "3. Ramon Martins", "4. Rafael Silveira"};        

    /**
     * Constructor of the Painting class
     */
    public Painting(int artist_id) {
        this.artist_id = artist_id;
        this.currentBid=0;
        this.owner=null;

    }
    /**
     * Get the artist ID of the painting
     */
    public int getArtistId() {
        return this.artist_id;
    }
    /**
     * Setter of owner
     */
    public void setOwner(Player p) {
        this.owner = p;
    }
    /**
     * Getter of owner
     */
    public Player getOwner() {
        return this.owner;
    }
    /**
     * Get the name of the artist
     */
    public String getArtistName() {
        return ARTIST_NAMES[artist_id];
    }
    /**
     * Sold the painting to the current bidder
     * This method has been completed for you.
     * You should not modify this method.
     */
    public void sold() {
        System.out.print("Sold! - ");
        if (currentBidder == null || owner == currentBidder) {
            System.out.println(this.toString() + " is sold to " + owner.getName() + " for " + currentBid);
            //owner get the painting automatically
            owner.buyPainting(this);
            owner.pay(currentBid); //owner pay to the bank
        } else {
            System.out.println(this.toString() + " is sold to " + currentBidder.getName() + " for " + currentBid);
            //currentBidder get the painting
            currentBidder.buyPainting(this);
            currentBidder.pay(currentBid);
            //owner get the money
            owner.earn(currentBid);
            owner = currentBidder;
        }

    }
    /**
     * toString method to be modified
     */
    public String toString() {
        if(this.owner!=null) {
            return this.getArtistName() + " [" + this.TYPE + "] owner: " + this.getOwner().getName();
        }else{
            return this.getArtistName() + " [" + this.TYPE + "] owner: " + this.getOwner();

        }

    }
    /**
     * The auction method - open auction
     * 
     * In open auction, each player has a chance to bid for the painting.
     * If a player bids higher than the current bid, he becomes the current bidder.
     * We always start the auction with the first player in the players array.
     * When all other players have passed (either bidding 0 or bidding lower 
     * than the current bid), the painting is sold to the current bidder.
     * 
     * p.s. we model the auction in round-robin fashion although open auction 
     * allows player to bid at any time.
     * 
     */
    public void auction(Player[] players) {

        if (TYPE.equalsIgnoreCase("Open Auction")) {
            int indexFromHighest= 0 ;
            int higherBid = 0;
            boolean HigherBidMade =  true;
            int n= 0;

            while(HigherBidMade){
                HigherBidMade=false;
                int highestbidIndex=-1;
                int currentPlayerIndex = 0 ;
                for(int i = 0 ; i < players.length ; i++){
                    //System.out.println("Index: "+indexFromHighest);
                    //indexFromHighest++;
                    if(players[i] == currentBidder){break;}
                    //System.out.println(players[i].getName()  + " has $"+players[i].getMoney());
                    int bidAmount = players[i].bid(currentBid);
                    //System.out.println(bidAmount+" > "+higherBid);
                    if(bidAmount>higherBid){
                        higherBid=bidAmount;
                        currentBidder=players[i];
                        HigherBidMade=true;
                        //currentBid=higherBid;
                        //highestbidIndex=i;
                        //indexFromHighest=0;

                        //System.out.println("Higher Bid: "+higherBid);
                    }
                    //if(indexFromHighest > 1){break;}
                    //currentPlayerIndex++;
                    //if(currentPlayerIndex==3){currentPlayerIndex=0;}
                }
                //n=1;
            }
            currentBid=higherBid;
            sold();

            //System.out.println(this.owner+" sells to "+ currentBidder);

        }

    }
}
