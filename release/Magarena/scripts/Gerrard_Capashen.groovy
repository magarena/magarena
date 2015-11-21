[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "PN gains 1 life for each card in target opponent's hand.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = it.getHandSize();
                game.logAppendValue(player,amount);
                game.doAction(new ChangeLifeAction(player, amount))
            });
        }
    }
]
