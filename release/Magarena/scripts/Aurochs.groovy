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
            final MagicTargetFilter<MagicPermanent> filter = ATTACKING_AUROCHS.except(creature);
            final int power = event.getPlayer().getNrOfPermanents(filter);
            game.doAction(new MagicChangeTurnPTAction(creature,power,0));
        }
    }
]
