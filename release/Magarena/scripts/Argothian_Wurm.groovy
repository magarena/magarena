[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(),
                this,
                "PN may\$ sacrifice a land. If a he or she does, put SN on the top of controller's library."
            );
        }
        @Override
       public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                return new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_LAND);                
                
            } 
        }
    }
]
