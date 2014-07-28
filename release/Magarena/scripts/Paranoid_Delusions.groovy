[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$ puts the top three cards of his or her " +
                "library into his or her graveyard. Cipher."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicMillLibraryAction(it,3));
                game.doAction(new MagicCipherAction(event.getCardOnStack(),event.getPlayer()));
            });
        }
    }
]
