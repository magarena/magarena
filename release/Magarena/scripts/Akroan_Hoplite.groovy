[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +X/+0 until end of turn, where X is the number of attacking creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final int power = event.getPlayer().getNrOfPermanents(MagicTargetFilterFactory.ATTACKING_CREATURE)
            game.doAction(new MagicChangeTurnPTAction(creature,power,0));
        }
    }
]
