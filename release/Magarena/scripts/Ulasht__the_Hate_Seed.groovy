[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int red = permanent.getController().getNrOfPermanents(
                RED_CREATURE_YOU_CONTROL.except(permanent)
            );
            final int green = permanent.getController().getNrOfPermanents(
                GREEN_CREATURE_YOU_CONTROL.except(permanent)
            );
            final int amount = red + green;
            if (amount>0) {
                game.doAction(new MagicChangeCountersAction(permanent, MagicCounterType.PlusOne, amount));
            } 
            return MagicEvent.NONE;
        }
    }
]
