[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Reveal"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{2}{G}{G}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals the top card of his or her library. If it's a creature card, "+
                "PN puts it onto the battlefield. Otherwise, PN puts it into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(1);
            for (final MagicCard card : cards) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Creature)) {
                    game.doAction(new PutOntoBattlefieldAction(MagicLocationType.OwnersLibrary, card, player));
                } else {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
                    game.logAppendMessage(player, "${player.getName()} puts ${card.getName()} into the graveyard.")
                }
            }
        }
    }
]
