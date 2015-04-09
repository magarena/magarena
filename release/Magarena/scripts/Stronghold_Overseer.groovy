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
            Collection<MagicPermanent> targets =
                    game.filterPermanents(game.getPlayer(0),CREATURE_WITH_SHADOW);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,1,0));
            }
            targets = game.filterPermanents(game.getPlayer(0),CREATURE_WITHOUT_SHADOW);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeTurnPTAction(target,-1,0));
            }
        }
    }
]
