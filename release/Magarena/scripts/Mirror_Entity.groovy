[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump, true),
        "X/X"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}"),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                source,
                x,
                this,
                "Creatures PN controls become RN/RN and gain all creature types until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = event.getRefInt();
            final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
                @Override
                public void modPowerToughness(final MagicPermanent S, final MagicPermanent P, final MagicPowerToughness pt) {
                    pt.set(X,X);
                }
            };
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new BecomesCreatureAction(
                    it,
                    PT,
                    MagicStatic.AllCreatureTypesUntilEOT
                ));
            }
        }
    }
]
