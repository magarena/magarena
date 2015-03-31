[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "SN deals damage to PN equal to the number of nonbasic lands he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicTargetFilterFactory.NONBASIC_LAND_YOU_CONTROL);
            game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),amount));
            game.logAppendMessage(event.getPlayer()," ("+amount+")");
        }
    }
]
