[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final MagicPlayer player = permanent.getController();
			return player.filterPermanents(MagicTargetFilterFactory.permanentName("Biovisionary", MagicTargetFilterFactory.Control.You)).size() >=4 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN wins the game."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicLoseGameAction(event.getPlayer().getOpponent()));
        };
    }
]
