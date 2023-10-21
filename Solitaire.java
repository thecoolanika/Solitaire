import java.util.*;

/**
 * Solitaire creates a solitaire game. Solitaire is a 
 *          card game where the goal is to stack all the 
 *          cards by suit in order and can stack cards 
 *          on each other with alternating color in 
 *          descending order
 *      
 * @author Anika Pandey with assistance from Ms. Datar
 * 
 * @version 1/10/23
 */

public class Solitaire
{
    /**
     * main method
     * @param args array of Strings
     */
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;

    /**
     * constructor that creates a Solitaire object
     */
    public Solitaire()
    {
        foundations = new Stack[4];
        for(int i=0; i<4; i++)
            foundations[i] = new Stack();
        piles = new Stack[7];
        for(int i=0; i<7; i++)
            piles[i] = new Stack();
        stock = createStock();
        waste = new Stack();
        deal();
        display = new SolitaireDisplay(this);
    }

    /**
     * returns the top of the stock
     * @return Card at the top of the stack or null if empty
     */
    public Card getStockCard()
    {
        if(stock.isEmpty())
            return null;
        return stock.peek();
    }

    //returns the card on top of the waste,
    //or null if the waste is empty
    /**
     * returns the top of the waste
     * @return Card at the top of the waste or null if empty
     */
    public Card getWasteCard()
    {
        if(waste.isEmpty())
            return null;
        return waste.peek();
    }
    
    /**
     * pops and returns top the waste
     * @return Card at the top of the waste of null if empty
     */
    public Card popWasteCard()
    {
        if(waste.isEmpty())
            return null;
        return waste.pop();
    }
    
    /**
     * pushes a card to the top of the waste
     * @param card to push
     */
    public void pushWasteCard(Card card)
    {
        waste.push(card);
    }
    
    /**
     * gets the top of a foundation pile
     * @precondition: 0 <= index < 4
     * @postcondition: postcondition: returns the card on top of the given
     * @param index of foundation to get top of
     * @return Card at top of foundation at index
     */
    public Card getFoundationCard(int index)
    {
        if(foundations[index].isEmpty())
            return null;
        return foundations[index].peek();
    }

    /**
     * returns a pile at passed index
     * @precondition: 0 <= index < 7
     * @postcondition:  returns a reference to the given pile
     * @param index of pile to get
     * @return Stack<Card> pile at index
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }

    /**
     * returns a created stock 
     * @return Stack<Card> normal deck
     */
    public Stack<Card> createStock()
    {
        ArrayList<Card> cards =  new ArrayList<Card>();
        for(int i=0; i<52; i++)
        {
            if(i<13)
                cards.add(new Card(i%13+1, "d"));
            else if(i<26)
                cards.add(new Card(i%13+1, "h"));
            else if(i<39)
                cards.add(new Card(i%13+1, "c"));
            else
                cards.add(new Card(i%13+1, "s"));
        }
        Stack<Card> s = new Stack<Card>();
        while(cards.size()!=0)
        {
            int number = (int)(cards.size()*Math.random());
            s.push(cards.get(number));
            cards.remove(number);
        }
        return s;
    }

    /**
     * deals the cards for game prep
     */
    public void deal()
    {
        for(int i=0; i<piles.length; i++)
        {
            for(int j=0; j<=i; j++)
                piles[i].push(stock.pop());
            if(!piles[i].isEmpty())
                piles[i].peek().turnUp();
        }
    }

    /**
     * deals three cards onto the waste
     */
    public void dealThreeCards()
    {
        for(int i=0; i<3; i++)
        {
            Card card;
            if(!stock.isEmpty()) 
            {
                card = stock.pop();
                card.turnUp();
                waste.push(card);
            }
        }
    }

    /**
     * resets the stock for another iteration
     */
    public void resetStock()
    {
        Card card;
        while(!waste.isEmpty())
        {
            card = waste.pop();
            card.turnDown();
            stock.push(card);
        }
    }

    /**
     * called when the stock is clicked
     * deals cards to the waste and can reset the stock
     */
    public void stockClicked()
    {
        if(!display.isWasteSelected() && !display.isPileSelected())
        {
            if(!stock.isEmpty())
                dealThreeCards();
            else
                resetStock();
        }
    }

    /**
     * called when the waste is clicked
     * selects and unselects the card on the top of the waste
     */
    public void wasteClicked()
    {
        if(!waste.isEmpty() && !display.isWasteSelected() && !display.isPileSelected())
            display.selectWaste();
        else if(display.isWasteSelected())
            display.unselect();
    }

    /**
     * called when given foundation is clicked
     * moves cards to the foundation, selects, and unselects foundation
     * @precondition: 0 <= index < 4
     * @param index of foundation that is clicked
     */
    public void foundationClicked(int index)
    {
        if (!display.isFoundationSelected())
        {
            if(display.isWasteSelected() && canAddToFoundation(waste.peek(), index))
            {
                foundations[index].push(waste.pop());
                display.unselect();
            }
            else if(display.isPileSelected() && 
                    canAddToFoundation(piles[display.selectedPile()].peek(), index))
            {
                foundations[index].push(piles[display.selectedPile()].pop());
                display.unselect();
            }
            else
                display.selectFoundation(index);
        }
        else
            display.unselect();
    }

    //precondition:  0 <= index < 7
    //called when given pile is clicked
    /**
     * called when given pile is clicked
     * moves cards from pile, waste, and foundation to pile; 
     *      turns cards up; and selects and unselects piles
     * @param index of pile that is clicked
     */
    public void pileClicked(int index)
    {
        if(display.isPileSelected())
        {
            Stack<Card> toAdd = removeFaceUpCards(display.selectedPile());
            
            while(!toAdd.isEmpty() && canAddToPile(toAdd.peek(), index))
            {
                addToPile(toAdd, index);
            }  
            while(!toAdd.isEmpty())
            {
                addToPile(toAdd, display.selectedPile());
            }  
            display.unselect();
        }
        else if(display.isWasteSelected() && !waste.isEmpty())
        {
            if(canAddToPile(waste.peek(), index))
            {
                piles[index].push(waste.pop());
                display.unselect();
            }
        }
        else if(display.isFoundationSelected())
        {
            if(canAddToPile(foundations[display.selectedFoundation()].peek(), index))
            {
                piles[index].push(foundations[display.selectedFoundation()].pop());
                display.unselect();
            }
        }
        else if(!display.isWasteSelected() && !display.isPileSelected() 
                && !piles[index].isEmpty() && piles[index].peek().isFaceUp()) 
            display.selectPile(index);
        else if(display.isPileSelected() && display.selectedPile()==index)
            display.unselect();
        else if(!display.isWasteSelected() && !display.isPileSelected() 
                && !piles[index].isEmpty() && !piles[index].peek().isFaceUp())
            piles[index].peek().turnUp();
        
    }

    /**
     * checks if a card can be added to a certain pile
     * @param card to check if can be added
     * @param index of pile to check if can be added to
     * @return true if can add; otherwise false
     */
    public boolean canAddToPile(Card card, int index)
    {
        if (!piles[index].isEmpty()) 
        {
            if(!piles[index].isEmpty() && !piles[index].peek().isFaceUp())
                return false;
            if(!piles[index].isEmpty() && (piles[index].peek().getRank()-1 != card.getRank()))
                return false;
            if(!piles[index].isEmpty() && (piles[index].peek().getSuit().equals("d")
                    || piles[index].peek().getSuit().equals("h")))
                return(card.getSuit().equals("c") || card.getSuit().equals("s"));
            return(card.getSuit().equals("d") || card.getSuit().equals("h")); 
        }
        else 
            return(card.getRank() == 13);
    }

    /**
     * returns and removes the face up cards from a pile
     * @param index of pile to remove face up cards of
     * @return Stack<Card> of face up cards in reverse order(smallest to largest)
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> s = new Stack<Card>();
        while(!piles[index].isEmpty() && piles[index].peek().isFaceUp())
            s.push(piles[index].pop());
        return s;
    }

    /**
     * adds cards to a pile
     * @param cards to add to pile
     * @param index of pile to add cards to
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        while(!cards.isEmpty())
            piles[index].push(cards.pop());
    }

    /**
     * checks if a card can be added to a foundation
     * @param card to check if can be added to foundation
     * @param index of pile to add card to
     * @return true if can add; otherwise false
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        if(foundations[index].isEmpty())
            return(card.getRank() == 1);
        return(foundations[index].peek().getSuit().equals(card.getSuit()) 
                && foundations[index].peek().getRank()+1 == (card.getRank()));
    }
    
    /**
     * checks if game is over by checking foundations
     * @return true if game is over; otherwise false
     */
    public boolean gameOver() 
    {
        for(int i = 0; i<4; i++)
        {
            if(foundations[i].isEmpty() || foundations[i].peek().getRank()<13)
                return false;
        }
        return true;
    }
}