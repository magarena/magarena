[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "PN looks at target player's\$ hand."+
                "PN Draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                MagicPlayer player ->
                game.doAction(new MagicRevealAction(player.getHand()));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
