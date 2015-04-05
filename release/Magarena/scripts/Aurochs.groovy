[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +1/+0 until end of turn for each other attacking Aurochs."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final int power = event.getPlayer().getNrOfPermanents(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.ATTACKING_AUROCHS,
                    event.getPermanent()
                )
            );
            game.doAction(new MagicChangeTurnPTAction(creature,power,0));
        }
    }
]
