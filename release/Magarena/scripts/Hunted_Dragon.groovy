[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ puts three 2/2 white Knight creature tokens with first strike onto the battlefield."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new PlayTokensAction(
                    it,
                    TokenCardDefinitions.get("2/2 white Knight creature token with first strike"),
                    3
                )); 
            })
        }
    }
]
