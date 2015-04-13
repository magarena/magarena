[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump,true,1),
        "X/X"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
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
                public void modPowerToughness(
                    final MagicPermanent S,
                    final MagicPermanent P,
                    final MagicPowerToughness pt
                ) {
                    pt.set(X,X);
                }
            };
            final MagicPlayer you = event.getPlayer();
            final Collection<MagicPermanent> creatures = you.filterPermanents(CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new BecomesCreatureAction(
                    creature,
                    PT,
                    MagicStatic.AllCreatureTypesUntilEOT
                ));
            }
        }
    }
]
