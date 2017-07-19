[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "If the difference between PN's life total and target player's\$ life total is 5 or less, exchange life totals with that player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final int opponentLife = it.getLife();
                final int playerLife = player.getLife();
                if (Math.abs(opponentLife - playerLife) <= 5) {
                    game.doAction(new ChangeLifeAction(it, playerLife-opponentLife));
                    game.doAction(new ChangeLifeAction(player, opponentLife-playerLife));
                }
            });
        }
    }
]
