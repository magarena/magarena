[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int count = payedCost.getX() >= 5 ?
                2 * payedCost.getX() :
                payedCost.getX();
            game.doAction(new ChangeCountersAction(permanent.getController(),permanent,MagicCounterType.PlusOne,count));
            return MagicEvent.NONE;
        }
    }
]
