[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getNrOfPermanents(
                MagicTargetFilterFactory.CREATURE_PLUSONE_COUNTER_YOU_CONTROL.except(permanent)
            );
            if (amount>0) {
                game.doAction(new MagicChangeCountersAction(permanent, MagicCounterType.PlusOne, amount));
            } 
            return MagicEvent.NONE;
        }
    }
]
