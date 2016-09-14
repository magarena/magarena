[
    new MagicPermanentActivation(
        [MagicCondition.YOUR_UPKEEP_CONDITION],
        new MagicActivationHints(MagicTiming.None),
        "Swap"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "PN exchanges life totals with target opponent.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer opponent ->
                final MagicPlayer player = event.getPlayer();
                final int opponentLife = opponent.getLife();
                final int playerLife = player.getLife();
                game.doAction(new ChangeLifeAction(player, opponentLife-playerLife));
                game.doAction(new ChangeLifeAction(opponent, playerLife-opponentLife));
            });
        }
    }
]
