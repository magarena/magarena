def action = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), it, MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
        final MagicCardList topCards = new MagicCardList(event.getRefCardList());
        topCards.removeCard(it);
        topCards.shuffle();
        topCards.each({ game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary)) });
    });
}

[
    new OtherSpellIsCastTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
            return permanent.isFriend(spell);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
            final int cmc = spell.getConvertedCost();
            return new MagicEvent(
                permanent,
                cmc,
                this,
                "PN reveals the top ${cmc} cards of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int cmc = event.getRefInt();
            final MagicCardList revealedCards = player.getLibrary().getCardsFromTop(cmc);
            game.addEvent(new MagicEvent(
                source,
                new MagicMayChoice(new MagicFromCardListChoice(revealedCards, 1, false)),
                revealedCards,
                action,
                "PN may\$ cast a card revealed this way with converted mana cost ${cmc} or less\$ without paying its mana cost. " +
                "Put the rest on the bottom of PN's library in a random order."
            ));
        }
    }
]

