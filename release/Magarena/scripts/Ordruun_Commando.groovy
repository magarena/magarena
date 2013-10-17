[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Prevent"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{W}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Prevent the next 1 damage that would be dealt to SN this turn"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {    
            game.doAction(new MagicPreventDamageAction(event.getPermanent(),1));    
        }
    }
]
