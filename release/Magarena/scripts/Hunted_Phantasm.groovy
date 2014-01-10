[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Target opponent\$ puts five 1/1 red Goblin creature tokens onto the battlefield."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer opponent ->
               game.doAction(new MagicPlayTokensAction(
                opponent,
                TokenCardDefinitions.get("1/1 red Goblin creature token"),
                5
            )); 
            })
        }
    }
]
