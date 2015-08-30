package magic.game.state;

import java.util.ArrayList;
import java.util.List;
import magic.model.DuelPlayerConfig;

public class GamePlayerState {
    GamePlayerState() {}

    private DuelPlayerConfig player;
    private final List<GameCardState> library = new ArrayList<>();
    private final List<GameCardState> hand = new ArrayList<>();
    private final List<GameCardState> permanents = new ArrayList<>();
    private final List<GameCardState> graveyard = new ArrayList<>();
    private final List<GameCardState> exiled = new ArrayList<>();
    private int life;
    private String name;
    private int face;
    private String aiType = "";
    private String deckProfileColors = "";

    public void setPlayerDefinition(DuelPlayerConfig magicPlayerDefinition) {
        this.player = magicPlayerDefinition;
    }

    public DuelPlayerConfig getPlayerDefinition() {
        return player;
    }

    public void addToLibrary(final String cardName, final int quantity) {
        library.add(new GameCardState(cardName, quantity));
    }

    public List<GameCardState> getLibrary() {
        return library;
    }

    public void addToPermanents(String cardName, boolean isTapped, int quantity) {
        permanents.add(new GameCardState(cardName, quantity, isTapped));
    }

    public List<GameCardState> getPermanents() {
        return permanents;                
    }

    public void addToHand(String cardName, int quantity) {
        hand.add(new GameCardState(cardName, quantity));
    }

    public List<GameCardState> getHand() {
        return hand;
    }

    public void addToGraveyard(String cardName, int quantity) {
        graveyard.add(new GameCardState(cardName, quantity));
    }

    public List<GameCardState> getGraveyard() {
        return graveyard;
    }

    public void addToExiled(String cardName, int quantity) {
        exiled.add(new GameCardState(cardName, quantity));
    }

    public List<GameCardState> getExiled() {
        return exiled;
    }    

    public void setLife(int i) {
        this.life = i;
    }

    public int getLife() {
        return life;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAi() {
        return !aiType.isEmpty();
    }

    public int getFace() {
        return face;
    }

    public void setFace(int i) {
        this.face = i;
    }

    public void setAiType(String name) {
        this.aiType = name;
    }

    public String getAiType() {
        return aiType;
    }

    public String getDeckProfileColors() {
        return deckProfileColors;
    }

    public void setDeckProfileColors(final String colors) {
        this.deckProfileColors = colors == null ? "" : colors;
    }

}
