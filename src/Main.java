import java.util.*;
public class Main {
    // Properties
    static ArrayList<Card> knownCards = new ArrayList<>();
    static ArrayList<Card> deck = Deck.createDeck();
    static int[] handCount = new int[10];
    static int simulationCount = 0;
    static int total;

    // Main method -->
    public static void main(String[] args) {

        // Prompt the user to enter community cards
        promptKnownCards();
        // Print the size and contents of knownCards and deck (for screencast)
        System.out.println("\nSize of knownCards: " + knownCards.size() + "\n Contents of knownCards: ");
        for (Card card : knownCards) {
            System.out.print(card.toString() + ", ");
        }
        System.out.println("\nSize of deck: " + deck.size() + "\n Contents of deck: ");
        for (Card card : deck) {
            System.out.println(card.toString() + " ");
        }

        // Prompt the user to enter the number of times they would like to run the simulation
        simulationCount = 0;
        promptSimulationCount();
        // Print the contents of simulationCount (for screencast)
        System.out.println("\nsimulationCount: " + simulationCount);

        // Store simulationCount in another int variable, used for calculating the probability (CHANGE - added step)
        total = simulationCount;

        // Run the simulation!
        while (simulationCount > 0) {
            // Print out the contents of knownCards and simulationCount (for screencast)
            System.out.println("\nknownCards: ");
            for (Card card : knownCards) {
                System.out.println(card + " ");
            }
            System.out.println("simulationCount: " + simulationCount);

            // Draw two random Card objects from deck and add it to knownCards
            Collections.shuffle(deck);
            knownCards.add(deck.get(0));
            knownCards.add(deck.get(1));

            // Print out the content of knownCards before checking for the probabilities (for screencast)
            System.out.println("\nknownCards: ");
            for (Card card : knownCards) {
                System.out.println(card + " ");
            }
            // Print out the length of handCount (for screencast)
            System.out.println("length of handCount: " + handCount.length);

            // Call the checker method for each hand type.
            if (hasRF()) {
                handCount[0]++;
            }
            else if (hasSF()) {
                handCount[1]++;
            }
            else if (has4K()) {
                handCount[2]++;
            }
            else if (hasFH()) {
                handCount[3]++;
            }
            else if (hasF()) {
                handCount[4]++;
            }
            else if (hasS()) {
                handCount[5]++;
            }
            else if (has3K()) {
                handCount[6]++;
            }
            else if (has2P()) {
                handCount[7]++;
            }
            else if (has1P()) {
                handCount[8]++;
            }
            else {
                handCount[9]++;
            }

            // Remove the last two objects of knownCards
            knownCards.remove(5);
            knownCards.remove(5);
            // Decrement simulationCount
            simulationCount--;
        }

        // Call printProbabilities method
        printProbabilities();

        // Print sum of handsCount array (for screencast)
        int sum = 0;
        for (int i : handCount) {
            sum += i;
        }
        System.out.println("Sum of handsCount array: " + sum + "\nsimulationCount: " + total);

    }

    // Other methods -->
    // 1. Prompt the user to enter the community cards and hole cards (CHANGE - added a method)
    public static void promptKnownCards() {
        Scanner scanner = new Scanner(System.in);
        String tempStr;

        while (knownCards.size() < 5) {
            System.out.println("Please enter the community cards and your hole cards in the format of \"value+suit\". \nE.g. \"KH\" for King of Hearts, \"AS\" for Ace of Spades, \"XC\" for Ten of Clubs." + "\nPlease enter your cards one by one.");
            tempStr = scanner.nextLine().toUpperCase(); // Convert user input to uppercase for consistency

            // Validate input and create a card
            if (tempStr.length() == 2 && isValidCard(tempStr)) {
                char value = tempStr.charAt(0);
                char suit = tempStr.charAt(1);

                Card tempCard = new Card(value, suit);

                // Check if the card is in the deck
                try {
                    if (Deck.containsCard(deck, tempCard)) {
                        deck.remove(Deck.findCardIndex(deck, tempCard)); // Remove card based on index found
                        knownCards.add(tempCard);
                        System.out.println("Card '" + value + suit + "' removed from deck and added to known cards.");
                    } else {
                        System.out.println("This card is not available in the deck or has already been chosen. Please try again.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid card input: " + e.getMessage());
                }
            }
        }
    }

    // 1.2 isValidCard helper method (CHANGE - added method)
    private static boolean isValidCard(String card) {
        String validValues = "23456789XJQKA";
        String validSuits = "DCHS"; // Diamonds, Clubs, Hearts, Spades
        return card.length() == 2 && validValues.indexOf(card.charAt(0)) != -1 && validSuits.indexOf(card.charAt(1)) != -1;
    }

    // 2. Prompt the user to enter the number of times they would like to run the simulation
    public static void promptSimulationCount() {
        Scanner scanner = new Scanner(System.in);

        while (simulationCount <= 0) { // Changed condition to <= 0 to avoid negative numbers
            System.out.println("\nPlease enter the number of times you would like to run this simulation.");
            try {
                simulationCount = scanner.nextInt();
                if (simulationCount <= 0) { // Check for positive values
                    System.out.println("Please enter a positive integer.");
                    simulationCount = 0; // Reset to continue loop in case of non-positive integers
                }
            } catch (Exception e) {
                System.out.println("Please enter a positive integer.");
                scanner.nextLine();
            }
        }
    }

    // 3. Royal Flush checker
    public static boolean hasRF() {
        boolean sameSuit = false;
        char suit = knownCards.get(0).getSuit();

        for (Card card : knownCards) {
            if (suit == card.getSuit()) {
                sameSuit = true;
            }
        }

        // Create ArrayList of flushValues
        ArrayList<Character> flushValues = new ArrayList<>();
        flushValues.add('X');
        flushValues.add('J');
        flushValues.add('Q');
        flushValues.add('K');
        flushValues.add('A');

        // Call helper method to check for Royal Flush
        if (sameSuit && hasRoyalFlushValues(knownCards, flushValues, suit)) {
            return true;
        }

        return false;
    }

    // 3.2 Helper method to check if an ArrayList contains the Royal Flush values (CHANGE - added a method)
    private static boolean hasRoyalFlushValues(ArrayList<Card> cards, ArrayList<Character> requiredValues, char suit) {
        // Create an array to keep track of found values
        boolean[] valueFound = new boolean[requiredValues.size()];

        for (Card card : cards) {
            if (card.getSuit() == suit) {
                int index = requiredValues.indexOf(card.getValue());
                if (index != -1) {
                    valueFound[index] = true;
                }
            }
        }

        // Check if all required values were found
        for (boolean found : valueFound) {
            if (!found) {
                return false;
            }
        }

        return true;
    }

    // 4. Straight Flush checker
    public static boolean hasSF() {
        ArrayList<Character> diamonds = new ArrayList<>();
        ArrayList<Character> clubs = new ArrayList<>();
        ArrayList<Character> hearts = new ArrayList<>();
        ArrayList<Character> spades = new ArrayList<>();

        // Distribute cards into these ArrayLists based on their suits
        for (Card card : knownCards) {
            switch(card.getSuit()) {
                case 'D':
                    diamonds.add(card.getValue());
                    break;
                case 'C':
                    clubs.add(card.getValue());
                    break;
                case 'H':
                    hearts.add(card.getValue());
                    break;
                case 'S':
                    spades.add(card.getValue());
                    break;
            }
        }

        // Check each suit for a Straight Flush (call the helper method)
        return isSF(diamonds) || isSF(clubs) || isSF(hearts) || isSF(spades);
    }

    // 4.2 Helper method to check if an ArrayList contains five consecutive values (CHANGE - added a method)
    private static boolean isSF(ArrayList<Character> values) {
        if (values.size() < 5) {
            return false;
        }

        Collections.sort(values); // Sort the values
        for (int i = 0; i < values.size() - 4; i++) { // Check for 5 consecutive values
            if (values.get(i + 4) == values.get(i) + 4) {
                return true;
            }
        }
        return false;
    }

    // 5. Four of a Kind checker
    public static boolean has4K() {
        // Create an array to count occurrences of each value
        int[] valueCounter = new int[256]; // ASCII table size to cover all values

        // Count each card's value
        for (Card card : knownCards) {
            char value = card.getValue();
            valueCounter[value]++;
        }

        // Check if any value count reached 4
        for (int count : valueCounter) {
            if (count == 4) {
                return true;
            }
        }

        return false;
    }

    // 6. Full House checker
    public static boolean hasFH() {
        // Array to count occurrences of each value
        int[] valueCounter = new int[256];

        for (Card card : knownCards) {
            char value = card.getValue();
            valueCounter[value]++;
        }

        // Exactly one triplet and one pair is needed
        boolean foundTriplet = false;
        boolean foundPair = false;

        for (int count : valueCounter) {
            if (count == 3) {
                foundTriplet = true;
            } else if (count == 2) {
                foundPair = true;
            }
        }

        return foundTriplet && foundPair;
    }

    // 7. Flush checker
    public static boolean hasF() {
        // Array to count occurrences of each suit
        int[] suitCounter = new int[4];

        for (Card card : knownCards) {
            int index = getSuitIndex(card.getSuit());
            suitCounter[index]++;

            // Check if there's a flush
            if (suitCounter[index] == 5) {
                return true;
            }
        }
        return false;
    }

    // 7.2 getSuitIndex helper method  (CHANGE - added a method)
    // Maps suit characters to specific indices
    private static int getSuitIndex(char suit) {
        switch (suit) {
            case 'D': // Diamonds
                return 0;
            case 'C': // Clubs
                return 1;
            case 'H': // Hearts
                return 2;
            case 'S': // Spades
                return 3;
            default:
                throw new IllegalArgumentException("Unknown suit: " + suit);
        }
    }

    // 8. Straight checker
    public static boolean hasS() {
        ArrayList<Integer> indices = new ArrayList<>();

        for (Card card : knownCards) {
            indices.add(getCardIndex(card.getValue()));
        }
        Collections.sort(indices);

        // Check for consecutive values
        for (int i = 0; i < indices.size() - 1; i++) {
            if (indices.get(i + 1) - indices.get(i) != 1) {
                return false;
            }
        }

        return true;
    }

    // 8.2 getCardIndex helper method  (CHANGE - added a method)
    // Maps card value characters to numerical ranks
    private static int getCardIndex(char value) {
        switch (value) {
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            case 'X': return 10;
            case 'J': return 11;
            case 'Q': return 12;
            case 'K': return 13;
            case 'A': return 14;
            default: throw new IllegalArgumentException("Unknown card value: " + value);
        }
    }

    // 9. Three of a Kind checker
    public static boolean has3K() {
        // Create an array to count occurrences of each value
        int[] valueCounter = new int[256]; // ASCII table size to cover all values

        // Count each card's value
        for (Card card : knownCards) {
            char value = card.getValue();
            valueCounter[value]++;
        }

        // Check if any value count reached 3
        for (int count : valueCounter) {
            if (count == 3) {
                return true;
            }
        }

        return false;
    }

    // 10. Two Pair checker
    public static boolean has2P() {
        // Array to count occurrences of each card value
        int[] valueCounter = new int[15];

        // Count each card's value
        for (Card card : knownCards) {
            int index = getCardIndex(card.getValue()); // Call the getCardIndex helper method from 8.2
            valueCounter[index]++;
        }

        // Count how many values have exactly two occurrences
        int pairCount = 0;
        for (int count : valueCounter) {
            if (count == 2) {
                pairCount++;
            }
        }

        // Check if there are exactly two pairs
        return pairCount == 2;
    }

    // 11. One Pair checker
    public static boolean has1P() {
        // Array to count occurrences of each card value
        int[] valueCounter = new int[15];

        // Count each card's value
        for (Card card : knownCards) {
            int index = getCardIndex(card.getValue()); // Call the getCardIndex helper method from 8.2
            valueCounter[index]++;
        }

        // Count how many values have exactly two occurrences
        int pairCount = 0;
        for (int count : valueCounter) {
            if (count == 2) {
                pairCount++;
            }
        }

        // Check if there is exactly one pair
        return pairCount == 1;
    }

    // 12. printProbabilities method
    public static void printProbabilities() {
        // Probabilities stored in an array of doubles (for screencast)
        double[] probabilities = new double[10];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = (double) handCount[i] / total;
        }

        double probSum = 0;
        for (double num : probabilities) {
            probSum += num;
        }


        // Print out the odds of each hand based on the simulation results
        System.out.println("\nBased on the simulation, the odds of drawing each hand are as follows.");

        System.out.println("Royal Flush: " + probabilities[0]);
        System.out.println("Straight Flush: " + probabilities[1]);
        System.out.println("Four of a Kind: " + probabilities[2]);
        System.out.println("Full House: " + probabilities[3]);
        System.out.println("Flush: " + probabilities[4]);
        System.out.println("Straight: " + probabilities[5]);
        System.out.println("Three of a Kind: " + probabilities[6]);
        System.out.println("Two Pair: " + probabilities[7]);
        System.out.println("One Pair: " + probabilities[8]);
        System.out.println("High Card: " + probabilities[9]);


        // Print out the sum of probabilities (for screencast)
        System.out.println("\nThe probabilities add up to: " + probSum);
    }
}
