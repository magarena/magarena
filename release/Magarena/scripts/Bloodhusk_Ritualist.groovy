[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) { 
            return permanent.isKicked() ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_OPPONENT,
                    this,
                    "Target opponent\$ discards " + permanent.getKicker() + " cards."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.addEvent(new MagicDiscardEvent(event.getSource(),player,event.getPermanent().getKicker(),false));
                }
            });
        }
    }
]
