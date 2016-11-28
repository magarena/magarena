[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = permanent.getChosenPlayer().getNrOfPermanents(MagicType.Creature);
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.MinusOne,amount));
            return MagicEvent.NONE;
        }
    }
]
