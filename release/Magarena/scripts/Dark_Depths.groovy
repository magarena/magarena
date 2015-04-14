def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new SacrificeAction(event.getPermanent()));
    game.doAction(new PlayTokenAction(
        event.getPlayer(), 
        TokenCardDefinitions.get("legendary 20/20 black Avatar creature token with flying and indestructible named Marit Lage")
    ));
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
                "Sacrifice SN and put a legendary 20/20 black Avatar creature token with flying and indestructible named Marit Lage onto the battlefield."
            )));
        }
    }
]
