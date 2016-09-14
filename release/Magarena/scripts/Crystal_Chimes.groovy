[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Recover"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{3}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN returns all enchantment cards from his or her graveyard to his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> targets = ENCHANTMENT_CARD_FROM_GRAVEYARD.filter(event);
            for (final MagicCard card : targets) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.Graveyard,
                    MagicLocationType.OwnersHand
                ));
            }
        }
    }
]
