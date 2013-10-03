[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            game.doAction(new MagicChangeTurnPTAction(permanent,2,0));  
            return MagicEvent.NONE;
        }
    }
]
