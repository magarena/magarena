def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new FlipAction(event.getPermanent()));
}

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.hasAbility(MagicAbility.Flying);
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new MagicPutStateTriggerOnStackAction(new MagicEvent(
                source,
                action,
                "Flip SN."
            )));
        }
    }
]
