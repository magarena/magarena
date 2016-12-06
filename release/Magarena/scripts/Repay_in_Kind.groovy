[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player's life total becomes the lowest life total among all players."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPlayer opponent = player.getOpponent();
            final int playerLife = player.getLife();
            final int opponentLife = opponent.getLife();
            final int amount = Math.min(playerLife, opponentLife);
            game.logAppendValue(player, amount);
            game.doAction(new ChangeLifeAction(player, amount - playerLife));
            game.doAction(new ChangeLifeAction(opponent, amount - opponentLife));
        }
    }
]

