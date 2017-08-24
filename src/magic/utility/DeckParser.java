package magic.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import magic.data.GeneralConfig;
import magic.data.settings.IntegerSetting;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;

public final class DeckParser {
    private DeckParser() {}

    // translatable strings
    private static final String _S2 = "Lines in file exceeds %d.";
    private static final String _S3 = "...more...";
    private static final String _S4 = "line %d: line length exceeds %d characters.";
    private static final String _S7 = "line %d: invalid card (%s).";

    private static final String CARD_QTY = "\\d+";
    private static final String CARD_NAME = "[A-záéû \"',-/]+";
    private static final Pattern QTY_PATTERN = Pattern.compile(CARD_QTY);
    private static final Pattern CARD_PATTERN = Pattern.compile(" " + CARD_NAME);
    private static final String MATCH1 = String.format("%s %s", CARD_QTY, CARD_NAME);
    private static final String MATCH2 = String.format("%s %s\\|.+", CARD_QTY, CARD_NAME);

    private static final int MAX_UNSUPPORTED_LINES = 50;
    private static final int MAX_LINE_ERRORS = 3;
    private static final int MAX_LINE_LENGTH = 50; // characters.
    private static final int MAX_LINES = GeneralConfig.get(IntegerSetting.DECK_MAX_LINES);

    public static MagicDeck parseLines(List<String> textLines) {

        final MagicDeck deck = new MagicDeck();

        if (textLines.isEmpty()) {
            return deck;
        }

        if (textLines.size() > MAX_LINES) {
            deck.setInvalidDeck(MText.get(_S2, MAX_LINES));
            return deck;
        }

        int unsupportedLines = 0;
        int lineNumber = 0;
        final List<String> lineErrors = new ArrayList<>();
        boolean isSkipLine = false;

        for (final String nextLine : textLines) {

            final String line = nextLine.trim();
            lineNumber++;

            /*
            ORDER IS IMPORTANT!
            */

            // ignore empty lines.
            if (line.isEmpty()) {
                continue;
            }

            // -------------------------------------------------------------
            // lines which can be greater than MAX_LINE_LENGTH.
            // -------------------------------------------------------------

            // magarena deck description.
            if (line.startsWith(">")) {
                deck.setDescription(line.substring(1));
                continue;
            }

            // comment lines -
            // # [magarena], // [???]
            if (line.startsWith("#") || (line.startsWith("/"))) {
                continue;
            }

            // -------------------------------------------------------------
            // lines which canNOT be greater than MAX_LINE_LENGTH.
            // -------------------------------------------------------------

            if (line.length() > MAX_LINE_LENGTH) {
                lineErrors.add(MText.get(_S4, lineNumber, MAX_LINE_LENGTH));
                continue;
            }

            // -------------------------------------------------------------
            // mtg.gamepedia.com markdown
            // -------------------------------------------------------------

            if (line.matches("<d title=\".+\">")) {
                // extract text between quotes...
                final Pattern regexp = Pattern.compile(".*\\\"(.*)\\\".*");
                final Matcher matcher = regexp.matcher(line);
                String deckName = (matcher.find() ? matcher.group(1) : "").trim();
                deck.setFilename("gamepedia." + deckName);
                continue;
            }

            if (line.startsWith("==") || line.startsWith("<")) {
                continue;
            }

            // -------------------------------------------------------------
            // Lines specfic to the deckbox.org deck format.
            // -------------------------------------------------------------
            if (line.startsWith("Sideboard:")) {
                isSkipLine = true;
            }

            // -------------------------------------------------------------
            // Lines specfic to the Forge deck format.
            // -------------------------------------------------------------

            // forge sections.
            if (line.startsWith("[")) {
                isSkipLine = !line.equals("[metadata]")
                    && !line.equalsIgnoreCase("[main]");
                continue;
            }

            if (isSkipLine) {
                continue;
            }

            // ignore properties in forge [metadata] section.
            if (line.startsWith("Format=")) {
                continue;
            }

            // forge deck name
            if (line.startsWith("Name=")) {
                deck.setFilename(line.substring(line.indexOf("=") + 1));
                continue;
            }

            // -------------------------------------------------------------
            // Lines specfic to the ??? deck format.
            // -------------------------------------------------------------

            // ignore sideboard cards.
            if (line.startsWith("SB:")) {
                continue;
            }

            // -------------------------------------------------------------
            // Lines containing card and quantity patterns.
            // -------------------------------------------------------------
            if (line.matches(MATCH1) || line.matches(MATCH2)) {
                // extract card quantity
                Matcher qtyMatcher = QTY_PATTERN.matcher(line);
                int quantity = Integer.parseInt(qtyMatcher.find() ? qtyMatcher.group() : "0");
                // extract card name
                Matcher cardMatcher = CARD_PATTERN.matcher(line);
                String cardName = (cardMatcher.find() ? cardMatcher.group() : "").trim();
                //
                MagicCardDefinition cardDef = DeckUtils.getCard(cardName);
                for (int count = quantity; count > 0; count--) {
                    deck.add(cardDef);
                }
                if (cardDef.isInvalid() || cardDef.isNonPlayable()) {
                    lineErrors.add(MText.get(_S7, lineNumber, cardDef.getName()));
                }
                continue;
            }

            // unsupported line.
            unsupportedLines++;
            if (unsupportedLines > MAX_UNSUPPORTED_LINES) {
                lineErrors.add("Maximum number of unrecognized lines exceeded.");
            }

            // abort if errors threshold is exceeded.
            if (lineErrors.size() > MAX_LINE_ERRORS) {
                lineErrors.remove(lineErrors.size() - 1);
                lineErrors.add(MText.get(_S3));
                deck.clear();
                break;
            }
        }

        if (lineErrors.size() > 0) {
            final StringBuilder sb = new StringBuilder();
            for (String lineError : lineErrors) {
                sb.append(lineError).append("\n");
            }
            deck.setInvalidDeck(sb.toString());
        }

        return deck;
    }

    public static MagicDeck parseText(String text) {

        // https://stackoverflow.com/questions/454908/split-java-string-by-new-line
        // String[] lines = text.split("\\r?\\n");
        String[] lines = text.split("[\\r\\n]+");

        return parseLines(Arrays.asList(lines));
    }

}
