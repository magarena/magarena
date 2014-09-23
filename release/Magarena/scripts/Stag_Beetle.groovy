[
    new MagicWhenComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int X = game.getNrOfPermanents(MagicType.Creature) - 1;
            game.doAction(MagicChangeCountersAction.Enters(permanent,MagicCounterType.PlusOne,X));
            return MagicEvent.NONE;
        }
    }
]
