import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i, 'Y');
                tiles[currentTile++] = new Tile(i, 'B');
                tiles[currentTile++] = new Tile(i, 'R');
                tiles[currentTile++] = new Tile(i, 'K');
            }
        }
    }

    // sorting each players hand
    public void sortTiles(Tile[] playerTiles, int count) {
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                // for number
                if (playerTiles[j].getValue() > playerTiles[j + 1].getValue()) {
                    Tile temp = playerTiles[j];
                    playerTiles[j] = playerTiles[j + 1];
                    playerTiles[j + 1] = temp;
                }
                // for color
                else if (playerTiles[j].getValue() == playerTiles[j + 1].getValue()) {
                    if (playerTiles[j].compareTo(playerTiles[j + 1]) > 0) {
                        Tile temp = playerTiles[j];
                        playerTiles[j] = playerTiles[j + 1];
                        playerTiles[j + 1] = temp;
                    }
                }
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        ArrayList<Tile> tilesArrList = new ArrayList<>();
        int tilesToGive;

        for (int i = 0; i < tiles.length; i++) {
            tilesArrList.add(tiles[i]);
        }

        for (int j = 0; j < players.length; j++) {
            if (j == 0) {
                tilesToGive = 15;
            } else {
                tilesToGive = 14;
            }

            for (int k = 0; k < tilesToGive; k++) {
                players[j].playerTiles[k] = tilesArrList.get(0);
                tilesArrList.remove(0);
            }
            players[j].numberOfTiles = tilesToGive;

            sortTiles(players[j].playerTiles, tilesToGive);

            for (int l = 0; l < tilesArrList.size(); l++) {
                tiles[l] = tilesArrList.get(l);
            }
        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we
     * picked
     */
    public String getLastDiscardedTile() {

        String result = "";
        if (lastDiscardedTile != null) {

            players[currentPlayerIndex].addTile(lastDiscardedTile);
            result = players[currentPlayerIndex].getName().toString() + "get the last discarded tile: "
                    + lastDiscardedTile.toString();

            lastDiscardedTile = null;
            return result;
        } else {
            result = "There is no discarded tile!";
            return result;
        }

    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top
     * tile)
     * it should return the toString method of the tile so that we can print what we
     * picked
     */
    public String getTopTile() {
        if (tiles.length == 0) {
            return "There is no more tiles in the stack.";
        }
        String topTile = tiles[tiles.length - 1].toString();

        players[currentPlayerIndex].addTile(tiles[tiles.length - 1]);

        Tile[] newTiles = new Tile[tiles.length - 1];

        System.arraycopy(tiles, 0, newTiles, 0, newTiles.length);

        tiles = newTiles;

        System.out.println(players[currentPlayerIndex].getName() + " Got top tile");
        return topTile;
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        ArrayList<Tile> tilesToShuffle = new ArrayList<>();

        for (int i = 0; i < tiles.length; i++) {
            tilesToShuffle.add(tiles[i]);
        }

        Collections.shuffle(tilesToShuffle);

        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = tilesToShuffle.get(i);
        }

    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        return players[currentPlayerIndex].isWinningHand() && this.tiles.length > 0;
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded
     * ones.
     */
    public void pickTileForComputer() {

        Player currentPlayer = players[currentPlayerIndex];
        Tile[] tiles = currentPlayer.getTiles();
        boolean shouldDiscard = true;
        if (currentPlayer.findPositionOfTile(lastDiscardedTile) != -1) {
            shouldDiscard = false;
        } else {
            int currentChainCount = 1;
            int minChainCount = 5;
            int minChainNumber = -1;
            int totalChainCount = 0;
            for (int i = 0; i < tiles.length; i++) {
                if (i != tiles.length - 1 && tiles[i].canFormChainWith(tiles[i + 1])) {
                    currentChainCount++;
                } else {
                    if (currentChainCount <= minChainCount) {
                        minChainCount = currentChainCount;
                        minChainNumber = tiles[i].getValue();
                    }
                    if (currentChainCount > 1)
                        totalChainCount++;
                    currentChainCount = 1;
                }
            }

            int possibleDiscardedChainCount = 1;
            if (!(minChainNumber == lastDiscardedTile.getValue())) {
                boolean chainStarted = false;
                for (int i = 0; i < tiles.length; i++) {
                    if (chainStarted) {
                        if (tiles[i].canFormChainWith(lastDiscardedTile)) {
                            possibleDiscardedChainCount++;
                        } else {
                            break;
                        }
                    } else {
                        if (tiles[i].canFormChainWith(lastDiscardedTile)) {
                            possibleDiscardedChainCount++;
                            chainStarted = true;
                        }
                    }
                }
            }
            if ((totalChainCount == 4 && possibleDiscardedChainCount < minChainCount)) {
                shouldDiscard = false;

            }

        }

        if (shouldDiscard) {
            System.out.println(
                    this.getLastDiscardedTile());
            ;

        } else {
            System.out.println(players[currentPlayerIndex].getName() + " picked the top tile of " + this.getTopTile());
            ;
            System.out.println("There are " + this.tiles.length + " tiles left.  ");
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {
        Player currentPlayer = players[currentPlayerIndex];
        Tile[] tiles = currentPlayer.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            if (i != tiles.length - 1 && tiles[i].compareTo(tiles[i + 1]) == 0) {
                this.discardTile(i);
                return;
            } else if (i < tiles.length - 3 && !tiles[i].canFormChainWith(tiles[i + 1])
                    && !tiles[i + 1].canFormChainWith(tiles[i + 2])) {
                this.discardTile(i + 1);
                return;
            }
        }
        int currentChainCount = 1;
        int minChainCount = 5;
        int minChainTileIndex = -1;
        for (int i = 0; i < tiles.length; i++) {
            if (i != tiles.length - 1 && tiles[i].canFormChainWith(tiles[i + 1])) {
                currentChainCount++;
            } else {
                if (currentChainCount <= minChainCount) {
                    minChainCount = currentChainCount;
                    minChainTileIndex = i;
                }
                currentChainCount = 1;
            }
        }

        this.discardTile(minChainTileIndex);

    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        if (tileIndex >= 0 && tileIndex < players[currentPlayerIndex].numberOfTiles) {
            lastDiscardedTile = players[currentPlayerIndex].playerTiles[tileIndex];
            players[currentPlayerIndex].getAndRemoveTile(tileIndex);
            displayDiscardInformation();
        }
    }

    public void displayDiscardInformation() {
        if (lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        System.out.println("------------------------------------");
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name, boolean isComputer) {
        if (index >= 0 && index <= 3) {
            players[index] = new Player(name, isComputer);
        }
    }

}
