package magic.ui.screen.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

class Keyword implements Comparable<Keyword> {

    private final String name;
    private final List<String> description = new ArrayList<>();
    private String[] cards = new String[]{};

    Keyword(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Keyword o) {
        return name.compareTo(o.name);
    }

    void setExampleCards(String[] cards) {
        this.cards = cards;
    }

    List<MagicCardDefinition> getExampleCards() {
        try {
            return Stream.of(cards)
                    .map(c -> CardDefinitions.getCard(c))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            System.err.println(ex);
            return new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    void addDescriptionLine(String line) {
        description.add(line);
    }

    String getName() {
        return name;
    }

    String getDescriptionAsHtml() {
        return description.stream()
                .collect(Collectors.joining("<br><br>", "<html>", "</html>"));
    }
}
