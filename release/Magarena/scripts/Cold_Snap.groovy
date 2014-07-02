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
            final int amount = player.getNrOfPermanents(MagicTargetFilterFactory.SNOW_LAND_YOU_CONTROL);
            final MagicDamage damage = new MagicDamage(event.getSource(),player,amount);
            game.logAppendMessage(event.getPlayer,"("+amount+")");
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
