package magic.model;

import java.util.Properties;
import magic.data.CardDefinitions;
import magic.data.DeckType;
import magic.model.player.PlayerProfile;

public class DuelPlayerConfig {

    private static final String PLAYER_DECK = "deckProfile";

    private MagicDeckProfile deckProfile;
    private final MagicDeck deck = new MagicDeck();
    private PlayerProfile playerProfile;

    // CTR
    public DuelPlayerConfig(final PlayerProfile aPlayerProfile, final MagicDeckProfile aDeckProfile) {
        playerProfile = aPlayerProfile;
        deckProfile = aDeckProfile;
    }

    public String getName() {
        return playerProfile.getPlayerName();
    }

    public void setDeckProfile(final MagicDeckProfile profile) {
        this.deckProfile = profile;
    }

    public MagicDeckProfile getDeckProfile() {
        return deckProfile;
    }

    public MagicDeck getDeck() {
        return deck;
    }

    public void setDeck(final MagicDeck aDeck) {
        deck.setContent(aDeck);
    }

    private static String getDeckPrefix(final String prefix, final int index) {
        return prefix + "deck" + index;
    }

    private void setDeckProfile(final String deckPropertyValue) {
        final DeckType deckType = DeckType.valueOf(deckPropertyValue.split(";", 0)[0]);
        final String deckValue = deckPropertyValue.split(";", 0)[1];
        setDeckProfile(MagicDeckProfile.getDeckProfile(deckType, deckValue));
    }

    private void loadDeck(final Properties properties, final String prefix) {
        deck.clear();
        for (int i = 1; i <= properties.size(); i++) {
            final String deckPrefix = getDeckPrefix(prefix, i);
            if (properties.containsKey(deckPrefix)) {
                final String cardName = properties.getProperty(deckPrefix);
                final MagicCardDefinition cdef = CardDefinitions.getCard(cardName);
                if (cdef.isValid()) {
                    deck.add(cdef);
                }
            }
        }        
    }

    public void load(final Properties properties, final String prefix) {
        setDeckProfile(
                properties.getProperty(
                        prefix + PLAYER_DECK,
                        DeckType.Random + ";" + MagicDeckProfile.ANY_THREE)
        );
        loadDeck(properties, prefix);    }

    public void save(final Properties properties, final String prefix) {

        properties.setProperty(prefix + PLAYER_DECK,
                deckProfile.getDeckType().name() + ";" + deckProfile.getDeckValue()
        );

        int index = 1;
        for (final MagicCardDefinition cardDefinition : deck) {
            properties.setProperty(getDeckPrefix(prefix, index++), cardDefinition.getFullName());
        }
    }

    public PlayerProfile getProfile() {
        return playerProfile;
    }

    public void setProfile(PlayerProfile aPlayerProfile) {
        playerProfile = aPlayerProfile;
    }

}
