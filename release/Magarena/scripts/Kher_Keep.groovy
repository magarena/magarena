[
    new MagicPermanentActivation(
        [
            MagicCondition.CAN_TAP_CONDITION,
            MagicManaCost.ONE_RED.getCondition()
        ],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_RED)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 0/1 red Kobold creature token named Kobolds of Kher Keep onto the battlefield."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Kobolds")
            ));
        }
    }
]
