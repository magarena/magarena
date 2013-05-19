[
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicCondition.HAS_CARD_CONDITION
        ] ,
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicDiscardEvent(source,source.getController(),1,false)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    }
]
