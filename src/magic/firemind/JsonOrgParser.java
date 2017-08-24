package magic.firemind;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import magic.utility.DeckUtils;
import magic.data.json.DownloadableJsonFile;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import org.json.JSONArray;
import org.json.JSONObject;

public final class JsonOrgParser {
    private JsonOrgParser() {}

    public static List<MagicDeck> parse(final File jsonFile) throws IOException {

        final List<MagicDeck> decks = new ArrayList<>();

        final JSONObject jsonRoot = new JSONObject(DownloadableJsonFile.getJsonString(jsonFile));
        final List<String> formats = new ArrayList<>(Arrays.asList(JSONObject.getNames(jsonRoot)));
        for (String format : formats) {
            final JSONObject jsonFormat = jsonRoot.getJSONObject(format);
            final String[] deckNamesArray = JSONObject.getNames(jsonFormat);
            if (deckNamesArray == null)
                continue;   // no decks specified for given format.

            final List<String> deckNames = new ArrayList<>(Arrays.asList(deckNamesArray));

            for (String deckName : deckNames) {
                final JSONObject jsonDeck = jsonFormat.getJSONObject(deckName);
                final MagicDeck deck = new MagicDeck();
                decks.add(deck);
                deck.setFilename(format + "." + deckName);
                deck.setDescription(getDeckDescription(jsonDeck));
                addCardsToDeck(deck, jsonDeck.getJSONArray("cards"));
            }
        }

        return decks;
    }

    private static void addCardsToDeck(final MagicDeck deck, final JSONArray jsonCards) {
        for (int i = 0; i < jsonCards.length(); i++) {
            final JSONObject jsonCard = jsonCards.getJSONObject(i);
            final String cardName = jsonCard.getString("name");
            final int cardQuantity = jsonCard.getInt("quantity");
            final MagicCardDefinition cardDef = DeckUtils.getCard(cardName);
            for (int j = 0; j < cardQuantity; j++) {
                deck.add(cardDef);
            }
        }
    }

    private static String getJsonString(JSONObject jsonDeck, String key) {
        return !jsonDeck.isNull(key) ? jsonDeck.getString(key) : "";
    }

    private static String getDeckDescription(final JSONObject jsonDeck) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Author: ").append(getJsonString(jsonDeck, "author"));
        sb.append("\nRating: ").append(jsonDeck.getString("rating"));
        sb.append("\nReleased: ").append(getFormattedReleaseDate(getJsonString(jsonDeck, "releaseDate")));
        if (!getJsonString(jsonDeck, "description").trim().isEmpty()) {
            sb.append("\n\n").append(jsonDeck.getString("description"));
        }
        return sb.toString();
    }

    private static String getFormattedReleaseDate(final String jsonValue) {
        if (jsonValue.length() != 8) {
            return jsonValue;
        } else {
            return jsonValue.substring(0, 4) + "-"
                    + jsonValue.substring(4, 6) + "-"
                    + jsonValue.substring(6, 8);
        }
    }



}
