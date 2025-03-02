import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name, boolean isComputer) {
        setName(name);
        playerTiles = new Tile[isComputer ? 14 : 15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the
                           // game
    }

    /*
     * TODO: removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        // Tile returninTile = playerTiles[index];
        // for (int i = index; i < playerTiles.length - 1; i++) {
        // playerTiles[i] = playerTiles[i + 1];
        // }
        // return returninTile;
        Tile[] arr_new = new Tile[playerTiles.length - 1];
        Tile removed = null;
        int j = index;
        for (int i = 0, k = 0; i < playerTiles.length; i++) {
            if (i != j) {
                arr_new[k] = playerTiles[i];
                k++;
            } else {
                removed = playerTiles[i];
            }
        }
        this.playerTiles = arr_new;
        this.numberOfTiles = arr_new.length;
        return removed;
    }

    /*
     * TODO: adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
    public void addTile(Tile t) {
        if (numberOfTiles >= 15) {
            System.out.println("Can not add more tiles");
            return;
        }

        int index = 0;
        while (index < numberOfTiles && t.getValue() >= playerTiles[index].getValue()) {
            if (t.getValue() == playerTiles[index].getValue()) {
                if (t.colorNameToInt() <= playerTiles[index].colorNameToInt()) {
                    break;
                }
                while (t.colorNameToInt() > playerTiles[index].colorNameToInt()) {
                    index++;
                }
            } else
                index++;
        }
        Tile[] arr_new = new Tile[15];
        for (int i = 0; i < index; i++) {
            arr_new[i] = playerTiles[i];
        }
        for (int i = numberOfTiles; i > index; i--) {
            arr_new[i] = playerTiles[i - 1];
        }
        arr_new[index] = t;
        playerTiles = arr_new;
        numberOfTiles++;
    }

    /*
     * TODO: checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * 
     * @return
     */
    public boolean isWinningHand() {
        int chainCount = 0;
        for (int i = 0; i < numberOfTiles - 1; i++) {
            ArrayList<Integer> colourValuesList = new ArrayList<>();

            colourValuesList.add(playerTiles[i].colorNameToInt());
            ArrayList<Tile> chain = new ArrayList<>();

            chain.add(playerTiles[i]);
            playerTiles[i].setChained(true);
            for (int j = 1; j < numberOfTiles; j++) {
                if (playerTiles[i].canFormChainWith(playerTiles[j]) && !playerTiles[j].isChained
                        && !colourValuesList.contains(playerTiles[j].colorNameToInt())) {// If they can form a chain and
                                                                                         // playerTiles j is not chained
                                                                                         // yet and the current chain
                                                                                         // does not contain that color
                    colourValuesList.add(playerTiles[j].colorNameToInt());
                    chain.add(playerTiles[j]);
                }
            }
            if (chain.size() == 4) {// Means we formed a chain
                for (int j = 0; j < chain.size(); j++) {
                    chain.get(j).setChained(true);
                    chainCount++;
                }
            }
        }
        return chainCount >= 3;// Means that player has 3 chains
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }

}
