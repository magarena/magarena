[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal)
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent("{1}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN scry 1. Put a landmark counter on SN. " +
                "Then if there are three or more landmark counters on SN, remove those counters, transfrom SN, " +
                "and create three colorless Treasure artifact tokens with \"{T}, Sacrifice this artifact: Add one mana of any color to your mana pool.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source = event.getPermanent();
            game.addEvent(new MagicScryEvent(event));
            game.doAction(new ChangeCountersAction(source, MagicCounterType.Landmark, 1));
            final int amount = source.getCounters(MagicCounterType.Landmark);
            if (amount >= 3) {
                game.doAction(new ChangeCountersAction(source, MagicCounterType.Landmark, -amount));
                game.doAction(new TransformAction(source));
                3.times {
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        CardDefinitions.getToken("colorless Treasure artifact token")
                    ));
                }
            }
        }
    }
]

