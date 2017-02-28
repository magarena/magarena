package magic.ui.deck.games;

import magic.ai.MagicAIImpl;
import magic.model.player.PlayerProfile;

class PlayerInfo {

    private MagicAIImpl aiType;
    private int aiLevel;
    private int aiXtraLife;
    private PlayerProfile humanProfile;

    void setAiType(String value) {
        if (value != null) {
            aiType = MagicAIImpl.valueOf(value);
        }
    }

    MagicAIImpl getAiType() {
        return aiType;
    }

    boolean isHuman() {
        return humanProfile != null;
    }

    void setAiLevel(int value) {
        aiLevel = value;
    }

    int getAiLevel() {
        return aiLevel;
    }

    void setAiXtraLife(int value) {
        aiXtraLife = value;
    }

    int getAiXtraLife() {
        return aiXtraLife;
    }

    void setHumanPlayerProfile(PlayerProfile profile) {
        humanProfile = profile;
    }

    String getHumanPlayerName() {
        return humanProfile.getPlayerName();
    }

}
