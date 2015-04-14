[
      new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Reveal"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{G}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Reveal the top card of PN's library. If it's a creature card, put it onto the battlefield. " + 
                "Otherwise, put it into PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicRevealAction(card));
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary
                ));
                if (card.hasType(MagicType.Creature)) {
                    game.doAction(new PlayCardAction(
                        card,
                        event.getPlayer()
                    ));
                } else {
                    game.doAction(new MoveCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.Graveyard
                    ));
                }
            }
        }
    }
]
