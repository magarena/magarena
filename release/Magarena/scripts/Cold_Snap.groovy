[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "SN deals damage to PN equal to the number of snow lands he or she controls."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(SNOW_LAND_YOU_CONTROL);
            game.logAppendMessage(event.getPlayer(),"("+amount+")");
            game.doAction(new MagicDealDamageAction(event.getSource(),player,amount));
        }
    }
]
