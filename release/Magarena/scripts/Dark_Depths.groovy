def action = {
    final MagicGame game, final MagicEvent event ->
    final SacrificeAction sac = new SacrificeAction(event.getPermanent())
    game.doAction(sac);
    if (sac.isValid()) {
        game.doAction(new PlayTokenAction(
            event.getPlayer(),
            CardDefinitions.getToken("legendary 20/20 black Avatar creature token with flying and indestructible named Marit Lage")
        ));
    }
}

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.Ice) == 0;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new PutStateTriggerOnStackAction(new MagicEvent(
                source,
                action,
                "Sacrifice SN. If PN does, create a legendary 20/20 black Avatar creature token with flying and indestructible named Marit Lage."
            )));
        }
    }
]
