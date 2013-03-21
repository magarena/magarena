[
    new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_BLUE_CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
    },
	new MagicAtUpkeepTrigger() {
    	@Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
        	return permanent.isController(upkeepPlayer) ? new MagicEvent( permanent, new MagicMayChoice( new MagicPayManaCostChoice(MagicManaCost.create("{U}{U}"))), this,"PN may pay {U}{U}. If you don't, sacrifice SN.") : MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
        	if (MagicMayChoice.isNoChoice(choiceResults[0])) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]