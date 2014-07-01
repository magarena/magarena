[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_PLAYER,
                this,
                "Target player\$ draws a card for each basic land type among lands he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                final int domain = player.getDomain();
                game.logAppendMessage(event.getPlayer()," ("+domain+")");
                game.doAction(new MagicDrawAction(player,domain));
            });
        }
    }
]
