package magic.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import magic.data.DeckType;
import magic.model.player.PlayerProfile;
import magic.utility.DeckParser;
import magic.utility.DeckUtils;

public class DuelPlayerConfig {

    private static final String PLAYER_DECK = "deckProfile";

    private MagicDeckProfile deckProfile;
    private MagicDeck deck = new MagicDeck();
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
        setDeckProfile(deck.getDeckType().name() + ";" + deck.getName());
    }

    private static String getDeckPrefix(final String prefix, final int index) {
        return prefix + "deck" + index;
    }

    public void setDeckProfile(final String deckPropertyValue) {
        final DeckType deckType = DeckType.valueOf(deckPropertyValue.split(";", 0)[0]);
        final String deckValue = deckPropertyValue.split(";", 0)[1];
        setDeckProfile(MagicDeckProfile.getDeckProfile(deckType, deckValue));
    }

    private long getDeckFileChecksum(Properties properties, String prefix) {
        try {
            return Long.valueOf(properties.getProperty(prefix + "deck.file.crc", "0"));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void loadDeck(final Properties properties, final String prefix) {
        Map<String, Integer> cards = new HashMap<>();
        for (int i = 1; i <= properties.size(); i++) {
            final String deckPrefix = getDeckPrefix(prefix, i);
            if (properties.containsKey(deckPrefix)) {
                final String cardName = properties.getProperty(deckPrefix);
                cards.put(cardName, cards.getOrDefault(cardName, 0) + 1);
            }
        }
        final StringBuilder sb = new StringBuilder();
        for (String cardName : cards.keySet()) {
            sb.append(cards.get(cardName))
                .append(" ")
                .append(cardName)
                .append("\n");
        }
        deck = DeckParser.parseText(sb.toString());
        deck.setFilename(properties.getProperty(prefix + "deck.name", ""));
        deck.setDeckType(DeckType.valueOf(properties.getProperty(prefix + "deck.file.type", "Random")));
        deck.setDescription(properties.getProperty(prefix + "deck.desc", ""));
        // only set non-zero checksum if original deck file on which duel
        // deck was based has not changed since the duel was created.
        long cs = getDeckFileChecksum(properties, prefix);
        deck.setDeckFileChecksum(cs > 0 && DeckUtils.getDeckFileChecksum(deck) == cs ? cs : 0);
    }

    public void save(final Properties properties, final String prefix) {

        properties.setProperty(prefix + "deck.file.crc", Long.toString(deck.getDeckFileChecksum()));
        properties.setProperty(prefix + "deck.file.type", deck.getDeckType().name());
        properties.setProperty(prefix + "deck.name", deck.getName());
        properties.setProperty(prefix + "deck.desc", deck.getDescription());

        properties.setProperty(prefix + PLAYER_DECK,
                deckProfile.getDeckType().name() + ";" + deckProfile.getDeckValue()
        );

        int index = 1;
        for (final MagicCardDefinition cardDefinition : deck) {
            properties.setProperty(getDeckPrefix(prefix, index++), cardDefinition.getDistinctName());
        }
    }

    public PlayerProfile getProfile() {
        return playerProfile;
    }

    public void setProfile(PlayerProfile aPlayerProfile) {
        playerProfile = aPlayerProfile;
    }

}
