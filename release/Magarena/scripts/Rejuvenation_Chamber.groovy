[
    new MagicPermanentActivation(
        [MagicCondition.CAN_TAP_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Life") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains 2 life"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),2));
        }
    }
]
