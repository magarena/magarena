[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Each player's life total becomes the highest life total among all players."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPlayer opponent = player.getOpponent();
            final int playerLife = player.getLife();
            final int opponentLife = opponent.getLife();
            final int amount = Math.max(playerLife, opponentLife);
            game.logAppendValue(player,amount);
            game.doAction(new ChangeLifeAction(player, amount-player.getLife()));
            game.doAction(new ChangeLifeAction(opponent, amount-opponent.getLife()));
        }
    }
]
