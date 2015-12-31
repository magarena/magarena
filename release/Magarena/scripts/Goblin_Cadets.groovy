[
    new ThisBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ gains control of SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new GainControlAction(it, event.getPermanent()));
            });
        }
    },

    new ThisBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ gains control of SN"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new GainControlAction(it, event.getPermanent()));
            });
        }
    }
]
