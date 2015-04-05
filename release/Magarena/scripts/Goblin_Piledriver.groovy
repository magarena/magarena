[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            new MagicEvent(
                permanent,
                this,
                "SN gets +2/+0 until end of turn for each other attacking Goblin."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.ATTACKING_GOBLIN,
                permanent
            );
            final int amount=game.filterPermanents(filter).size()*2;
            game.doAction(new MagicChangeTurnPTAction(permanent,amount,0));
        }
    }
]
