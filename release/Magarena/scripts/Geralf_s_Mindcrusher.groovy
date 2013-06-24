[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ puts the top five cards of " +
                "his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    game.doAction(new MagicMillLibraryAction(targetPlayer,5));
                }
            });
        }
    }
]
