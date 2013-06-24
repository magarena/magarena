[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$ puts the top three cards " +
                "of his or her library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicMillLibraryAction(player,3));
                }
            });
        }
    }
]
