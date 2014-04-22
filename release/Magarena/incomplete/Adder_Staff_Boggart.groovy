[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicAction clashAction = new MagicChangeCountersAction(permanent, MagicCounterType.PlusOne, 2, true);
            return new MagicClashEvent(permanent, permanent.getController(), clashAction);
        }
    }
]
