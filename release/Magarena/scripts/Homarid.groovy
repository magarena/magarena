def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeCountersAction(
        event.getPermanent(), 
        MagicCounterType.Tide,
        -event.getPermanent().getCounters(MagicCounterType.Tide)
    ));
}

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.Tide) == 4;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new PutStateTriggerOnStackAction(new MagicEvent(
                source,
                action,
                "PN removes all tide counters from SN."
            )));
        }
    }
]
