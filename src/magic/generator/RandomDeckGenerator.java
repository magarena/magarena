package magic.generator;

import java.util.ArrayList;
import java.util.List;

import magic.data.CardDefinitions;
import magic.data.DeckGenerator;
import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicRandom;

public class RandomDeckGenerator {

    private final List<MagicCardDefinition> spellCards = new ArrayList<>();
    private final List<MagicCardDefinition> landCards = new ArrayList<>();

    private MagicFormat cubeDefinition;

    public RandomDeckGenerator(final MagicFormat cubeDefinition) {
        this.cubeDefinition = cubeDefinition;
    }

    public RandomDeckGenerator() {
        this(MagicFormat.ALL);
    }

    public void setCubeDefinition(final MagicFormat cube) {
        cubeDefinition = cube;
    }

    private void genSpells() {
        if (cubeDefinition == null) {
            return;
        }

        spellCards.clear();
        final List<MagicCardDefinition> cardPool = CardDefinitions.getSpellCards();
        for (int rarity = getMinRarity(); rarity <= getMaxRarity(); rarity++) {
            for (final MagicCardDefinition card : cardPool) {
                if (card.getRarity() >= getMinRarity() && card.getRarity() <= rarity && cubeDefinition.isCardLegal(card)) {
                    spellCards.add(card);
                }
            }
        }
    }

    public int getMinRarity() {
        return 1;
    }

    public int getMaxRarity() {
        return 4;
    }

    private void genLands() {
        if (cubeDefinition == null) {
            return;
        }

        landCards.clear();
        CardDefinitions.getNonBasicLandCards()
            .filter(card -> cubeDefinition.isCardLegal(card))
            .forEach(card -> addCopiesToLandCardsList(card, 4));
    }

    private void addCopiesToLandCardsList(final MagicCardDefinition aCard, final int amount) {
        for (int count = amount; count > 0; count--) {
            landCards.add(aCard);
        }
    }

    public void generateDeck(final int size, final MagicDeckProfile profile, final MagicDeck deck) {

        final DeckGenerator deckGenerator = new DeckGenerator();
        deckGenerator.deckSize = size;
        deckGenerator.setDeckProfile(profile);
        deckGenerator.setDeck(deck);
        generateDeck(deckGenerator);
    }

    public void generateDeck(final DeckGenerator deckGenerator) {

        final MagicDeckProfile profile = deckGenerator.getDeckProfile();
        final MagicDeck deck = deckGenerator.getDeck();
        final int spells = deckGenerator.getSpellsCount();
        final int maxCreatures = deckGenerator.getMaxCreaturesCount();

        setColors(profile);

        final MagicCondensedDeck condensedDeck = new MagicCondensedDeck();

        genSpells();
        genLands();

        final int lands = profile.getNrOfNonBasicLands(deckGenerator.getLandsCount());

        final int maxNonlandNoncreature = spells - maxCreatures;
        final int maxColorless = spells/6;
        final int maxHigh = spells/6;
        final int maxOther = (spells-maxHigh)/2;
        final int[] maxCost = {maxOther,maxOther+1,maxHigh};

        int countCreatures = 0;
        int countColorless = 0;
        int countNonlandNoncreature = 0;
        final int[] countCost = new int[3];

        addRequiredSpells(condensedDeck);

        // count required cards added
        for (final MagicCardDefinition card : condensedDeck.toMagicDeck()) {
            if (card.isCreature()) {
                countCreatures++;
            } else if (!card.isLand()) {
                countNonlandNoncreature++;
            }

            if (card.isColorless()) {
                countColorless++;
            }

            countCost[card.getCostBucket()]++;
        }

        // Add spells to deck.
        boolean isGenSpellsCalled = false;
        while (condensedDeck.getNumCards() < spells && !spellCards.isEmpty()) {

            final int index = MagicRandom.nextRNGInt(spellCards.size());

            final MagicCardDefinition cardDefinition=spellCards.get(index);
            spellCards.remove(index);

            if (cardDefinition.isPlayable(profile)) {
                final boolean creature = cardDefinition.isCreature();

                if (creature && countCreatures >= maxCreatures) {
                    continue;
                }

                if (!creature && countNonlandNoncreature >= maxNonlandNoncreature) {
                    continue;
                }

                final boolean colorless = cardDefinition.isColorless();
                if (!ignoreMaxColorless() && colorless && countColorless >= maxColorless) {
                    continue;
                }

                final int bucket = cardDefinition.getCostBucket();
                if (!ignoreMaxCost() && countCost[bucket] >= maxCost[bucket]) {
                    continue;
                }

                if (condensedDeck.addCard(cardDefinition, false)) {
                    countCost[bucket]++;
                    if (creature) {
                        countCreatures++;
                    } else if (!cardDefinition.isLand()) {
                        countNonlandNoncreature++;
                    }
                    if (colorless) {
                        countColorless++;
                    }
                }
            }

            if (spellCards.isEmpty() && !isGenSpellsCalled) {
                // TODO: not too sure what this does yet but for a small set of cards
                // it can cause cause an endless loop by preventing spellCards from
                // reaching zero and triggering the condition in the while clause.
                // Hence the use of the boolean hack.
                genSpells();
                isGenSpellsCalled = true;
            }

        }

        // Add nonbasic lands to deck.
        addRequiredLands(condensedDeck);

        while (condensedDeck.getNumCards() < spells+lands && landCards.size() > 0) {
            final int index=MagicRandom.nextRNGInt(landCards.size());
            final MagicCardDefinition cardDefinition=landCards.get(index);
            landCards.remove(index);

            if (cardDefinition.isPlayable(profile)) {
                condensedDeck.addCard(cardDefinition, false);
            }
        }

        deck.setContent(condensedDeck.toMagicDeck());
    }

    protected void addRequiredCards(final MagicCondensedDeck deck, final String[] cards) {
        for (final String name : cards) {
            final MagicCardDefinition cardDef = CardDefinitions.getCard(name);
            if (cardDef.isValid()) {
                deck.addCard(cardDef, false);
            } else {
                System.err.println("Cannot find " + name);
            }
        }
    }

    public void addRequiredSpells(final MagicCondensedDeck deck) { }

    public void addRequiredLands(final MagicCondensedDeck deck) { }

    public void setColors(final MagicDeckProfile profile) {    }

    public boolean ignoreMaxColorless() {
        return false;
    }

    public boolean ignoreMaxCost() {
        return false;
    }
}
