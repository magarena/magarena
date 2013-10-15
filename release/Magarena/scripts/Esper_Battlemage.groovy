[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Prevent"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source,"{W}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Prevent the next 2 damage that would be dealt to PN this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPreventDamageAction(event.getPlayer(),2));
        }
    }
]
