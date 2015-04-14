[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Draw three cards. Target player\$ puts the top three cards of his or her " +
                "library into his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DrawAction(event.getPlayer(),3));
                game.doAction(new MagicMillLibraryAction(it,3));
            });
        }
    }
]
