package magic.generator;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeckProfile;
import magic.model.MagicSubType;
import magic.model.MagicRandom;

import java.util.ArrayList;
import java.util.HashMap;

public class Tribal_Mono_DeckGenerator extends RandomDeckGenerator {

    private static final int MIN_NUM_CARDS_WITH_SUBTYPE = 30;

    // all possible tribes - calculated once
    private static final ArrayList<MagicSubType> possibleTribes = new ArrayList<MagicSubType>();
    private static final ArrayList<ArrayList<String>> possibleColors = new ArrayList<ArrayList<String>>();

    // random tribe from all possible for each instance
    private final MagicSubType tribe;
    private final String colorText;

    public Tribal_Mono_DeckGenerator() {

        if (!hasChoice()) {
            getPossibleTribes();
        }

        if (hasChoice()) {
            final int i = MagicRandom.nextRNGInt(possibleTribes.size());
            tribe = possibleTribes.get(i);
            colorText = possibleColors.get(i).get(MagicRandom.nextRNGInt(possibleColors.get(i).size()));
        } else {
            tribe = null;
            colorText = "";
        }

    }

    private boolean hasChoice() {
        return possibleTribes.size() > 0 && possibleColors.size() == possibleTribes.size();
    }

    private void getPossibleTribes() {
        for (final MagicSubType s : MagicSubType.ALL_CREATURES) {
            final HashMap<MagicColor, Integer> countColors = new HashMap<MagicColor, Integer>();
            countColors.put(MagicColor.Black, 0);
            countColors.put(MagicColor.White, 0);
            countColors.put(MagicColor.Green, 0);
            countColors.put(MagicColor.Red, 0);
            countColors.put(MagicColor.Blue, 0);

            // count colors
            for (final MagicCardDefinition card : CardDefinitions.getDefaultPlayableCardDefs()) {
                if (card.hasSubType(s)) {
                    final int colorFlags = card.getColorFlags();

                    for (final MagicColor c : countColors.keySet()) {
                        if (c.hasColor(colorFlags)) {
                            countColors.put(c, countColors.get(c) + 1);
                        }
                    }
                }
            }

            final ArrayList<String> choiceColors = getPossibleColors(countColors);

            if (choiceColors.size() > 0) {
                possibleTribes.add(s);
                possibleColors.add(choiceColors);
            }
        }
    }

    private ArrayList<String> getPossibleColors(final HashMap<MagicColor, Integer> countColors) {
        // monocolor
        final ArrayList<String> a = new ArrayList<String>();

        for (final MagicColor c : countColors.keySet()) {
            if (countColors.get(c).intValue() > MIN_NUM_CARDS_WITH_SUBTYPE) {
                a.add("" + c.getSymbol());
            }
        }

        return a;
    }

    public String getColorText() {
        return colorText;
    }

    @Override
    public int getMinRarity() {
        return 1;
    }

    @Override
    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        if (hasChoice()) {
            return !card.isCreature() || card.hasSubType(tribe);
        } else {
            return true;
        }
    }

    @Override
    public void setColors(final MagicDeckProfile profile) {
        profile.setColors(getColorText());
    }

    @Override
    public boolean ignoreMaxCost() {
        return false;
    }
}
