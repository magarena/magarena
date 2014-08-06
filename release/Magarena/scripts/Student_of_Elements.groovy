def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicFlipAction(event.getPermanent()));
}

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.hasAbility(MagicAbility.Flying);
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            final String desc = "Flip Student of Elements."
            if (game.getStack().hasItem(source, desc) == false) {
                game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(new MagicEvent(
                    source,
                    action,
                    desc
                ))));
            }
        }
    }
]
