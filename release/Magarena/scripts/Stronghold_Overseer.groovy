[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump,1),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{B}{B}"),
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Creatures with shadow get +1/+0 until end of turn and " +
                "creatures without shadow get -1/-0 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_WITH_SHADOW.filter(game) each {
                game.doAction(new MagicChangeTurnPTAction(it,1,0));
            }
            CREATURE_WITHOUT_SHADOW.filter(game) each {
                game.doAction(new MagicChangeTurnPTAction(it,-1,0));
            }
        }
    }
]
