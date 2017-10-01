[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final MagicPermanent equipped = permanent.getEquippedCreature();
            return (equipped == died) ?
                new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    this,
                    "Target opponent\$ gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new GainControlAction(it, event.getPermanent()));
            })
        }
    }
]
