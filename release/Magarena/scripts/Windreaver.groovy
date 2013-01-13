[
    new MagicPermanentActivation(
        [MagicManaCost.BLUE.getCondition()],
        new MagicActivationHints(MagicTiming.Pump),
        "Switch") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.BLUE)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Switch SN's power and toughness until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicAddStaticAction(event.getPermanent(), new MagicStatic(
                    MagicLayer.SwitchPT,
                    MagicStatic.UntilEOT) {
                @Override
                public void modPowerToughness(
                        final MagicPermanent source,
                        final MagicPermanent permanent,
                        final MagicPowerToughness pt) {
                    pt.set(pt.toughness(),pt.power());
                }   
            }));
        }
    }
]
