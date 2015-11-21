[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Each opponent loses 1 life. PN gains life equal to the amount lost this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final ChangeLifeAction act = new ChangeLifeAction(player.getOpponent(),-1);
            game.doAction(act);
            game.doAction(new ChangeLifeAction(player,-act.getLifeChange()));
        }
    }
]
