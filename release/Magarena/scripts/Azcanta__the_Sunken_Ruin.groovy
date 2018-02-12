def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList topCards = new MagicCardList(event.getRefCardList());
    event.processChosenCards(game, {
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
        topCards.removeCard(it);
    });
    topCards.each {
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary))
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "AddToHand"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{U}"),
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN looks at the top four cards of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList topCards = player.getCardsFromTop(4);
            game.doAction(new LookAction(topCards, player, "top four cards of your library"));
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(
                    topCards.findAll({ !it.hasType(MagicType.Creature) && !it.hasType(MagicType.Land) }),
                    1,
                    true
                ),
                topCards,
                action,
                "PN may reveal a noncreature, nonland card from among them\$ and put it into PN's hand. " +
                "PN puts the rest on the bottom of PN's library in any order."
            ));
        }
    }
]

