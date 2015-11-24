[
    new SelfDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final int amount = permanent.getPower();
            return new MagicEvent(
                permanent,
                NEG_TARGET_PLAYER,
                amount,
                this,
                "Target player\$ loses life equal to SN's power. (${amount})"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new ChangeLifeAction(it, -event.getRefInt()));
            });
        }
    }
]
