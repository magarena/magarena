[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Each opponent loses 5 life. PN gains life equal to the life lost this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            final ChangeLifeAction act = new ChangeLifeAction(player.getOpponent(),-5);
            game.doAction(act);
            game.doAction(new ChangeLifeAction(player,-act.getLifeChange()));
        }
    }
]
