import java.util.ArrayList;

public class Card {
    // Properties
    private char values;
    private char suits;

    // Constructor
    public Card(char values, char suits) {
        this.values = values;
        this.suits = suits;
    }

    // Behaviors

    // Getters (CHANGE - added methods)
    public char getValue() {
        return values;
    }

    public char getSuit() {
        return suits;
    }

    // toString method (CHANGE - added a method for debugging purposes)
    public String toString() {
       return "" + values+suits;
    }
}
