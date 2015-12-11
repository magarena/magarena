[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            final MagicPlayer you = permanent.getController();
            return you.getNrOfPermanents(new MagicNameTargetFilter("Biovisionary")) >=4 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN wins the game."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(new MagicNameTargetFilter("Biovisionary")) >=4) {
                game.doAction(new LoseGameAction(event.getPlayer().getOpponent()));
            }
        }
    }
]
