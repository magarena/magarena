[
    new MagicPermanentActivation(
        [MagicCondition.TWO_OR_MORE_WHITE_PERMANENTS],
        new MagicActivationHints(MagicTiming.Main),
        "Card"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CARD_FROM_GRAVEYARD,
                this,
                "Put target card\$ from your graveyard on the bottom of your library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new RemoveCardAction(it,MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(
                    it,
                    MagicLocationType.Graveyard,
                    MagicLocationType.BottomOfOwnersLibrary
                ));
            });
        }
    }
]
