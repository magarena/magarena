[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getLifeGainThisTurn();
            game.doAction(ChangeCountersAction.Enters(permanent,MagicCounterType.PlusOne,amount));
            return MagicEvent.NONE;
        }
    }
]
