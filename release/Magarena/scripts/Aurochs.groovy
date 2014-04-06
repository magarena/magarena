[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+0 until end of turn for each other attacking Aurochs."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final int power = event.getPlayer().getNrOfPermanents(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.TARGET_ATTACKING_AUROCHS,
                    event.getPermanent()
                )
            );
            game.doAction(new MagicChangeTurnPTAction(creature,power,0));
        }
    }
]
