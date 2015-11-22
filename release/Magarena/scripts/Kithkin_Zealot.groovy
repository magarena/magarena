[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "PN gains 1 life for each black and/or red permanent target opponent\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer opponent ->
                final MagicPlayer player = event.getPlayer();
                final int amount = BLACK_OR_RED_PERMANENT.filter(opponent).size();
                game.logAppendValue(player,amount);
                game.doAction(new ChangeLifeAction(player, amount));
            });
        }
    }
]
