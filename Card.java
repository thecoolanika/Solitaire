
/**
 * Card is a normal card with a rank, suit, and can be faceup or facedown used
 *          in the solitaire game
 *          
 * @author Anika Pandey
 * @version 1/10/23
 */
public class Card
{
    private int rank;
    private String suit;
    private boolean faceUp;

    /**
     * constructor for objects of class Card
     * @param r value of rank
     * @param s suit type
     */
    public Card(int r, String s)
    {
        rank = r;
        suit = s;
        faceUp = false;
    }

    /**
     * gets the rank of the card
     * @return Int rank of the card
     */
    public int getRank()
    {
        return rank;
    }
    
    /**
     * gets the suit of the card
     * @return String suit of the card
     */
    public String getSuit()
    {
        return suit;
    }
    
    /**
     * checks if the card is red in color
     * @return true if red; otherwise false
     */
    public boolean isRed()
    {
        return(suit.equals("d") || suit.equals("h"));
    }
    
    /**
     * checks if the card if face up
     * @return true if faceup; otherwise false
     */
    public boolean isFaceUp()
    {
        return faceUp;
    }
    
    /**
     * turns up a card
     */
    public void turnUp()
    {
        faceUp = true;
    }
    
    /**
     * turns down a card
     */
    public void turnDown()
    {
        faceUp = false;
    }
    
    /**
     * gets the file name of a card with its rank and suit
     * @return String of filname
     */
    public String getFileName()
    {
        if(!faceUp)
            return "cards/back.gif";
        if(rank==1)
            return "cards/a" + suit + ".gif";
        if(rank<10)
            return "cards/" + rank + suit + ".gif";
        if(rank==10)
            return "cards/t" + suit + ".gif";
        if(rank==11)
            return "cards/j" + suit + ".gif";
        if(rank==12)
            return "cards/q" + suit + ".gif";
        return "cards/k" + suit + ".gif";    
    }
    
}
