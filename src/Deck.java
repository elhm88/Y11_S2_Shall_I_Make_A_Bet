import java.util.*;
public class Deck {
    // Properties
    private Card card;

    // Constructor
    public Deck(Card card) {
        this.card = card;
    }

    // Behaviours

    // createDeck method
    public static ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();

        char[] values = {'2', '3', '4', '5', '6', '7', '8', '9', 'X', 'J', 'Q', 'K', 'A'};
        char[] suits = {'D', 'C', 'H', 'S'};

        for (char suit : suits) {  // Loop through values
            for (char value : values) { // Loop through suits
                deck.add(new Card(value, suit));
            }
        }
        return deck;
    }

    // addCard method (CHANGE - added a method)
    public static Card addCard(char values, char suits) {
        Card card = new Card(values, suits);
        return card;
    }

    // containsCard method (CHANGE - added a method)
    public static boolean containsCard(ArrayList<Card> deck, Card card) {
        for (Card c : deck) {
            if (c.getValue() == card.getValue() && c.getSuit() == card.getSuit()) {
                return true;
            }
        }
        return false;
    }

    // findCardIndex method (CHANGE - added a method)
    public static int findCardIndex(ArrayList<Card> deck, Card card) {
        for (int i = 0; i < deck.size(); i++) {
            Card c = deck.get(i);
            if (c.getValue() == card.getValue() && c.getSuit() == card.getSuit()) {
                return i;
            }
        }
        return -1; // Return -1 if card is not found
    }

}
