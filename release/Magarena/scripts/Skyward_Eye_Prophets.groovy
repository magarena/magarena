[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Reveal"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals the top card of his or her library. If it's a land card, "+
                "PN puts it onto the battlefield. Otherwise, PN puts it into his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = player.getLibrary().getCardsFromTop(1);
            for (final MagicCard card : cards) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Land)) {
                    game.doAction(new PutOntoBattlefieldAction(MagicLocationType.OwnersLibrary, card, player));
                } else {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                }
            }
        }
    }
]
