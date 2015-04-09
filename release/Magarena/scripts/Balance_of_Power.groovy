[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "If target opponent\$ has more cards in hand than PN, PN draws cards equal to the difference."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,{
                final MagicPlayer player = event.getPlayer();
                final int amount = it.getHandSize() - player.getHandSize();
                if (amount > 0) {
                    game.doAction(new MagicDrawAction(player,amount));
                }
                if (amount==1) {
                    game.logAppendMessage(player, "${player.getName()} draws (${amount}) card.");
                } else {
                    game.logAppendMessage(player, "${player.getName()} draws (${amount}) cards.");
                }
            });
        }
    }
]
