[
    new MagicWhenComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getDevotion(MagicColor.Green);
            game.doAction(MagicChangeCountersAction.Enters(permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    }
]
