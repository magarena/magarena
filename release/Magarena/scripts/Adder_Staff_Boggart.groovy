def clashAction = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1));
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicClashEvent(permanent, permanent.getController(), clashAction);
        }
    }
]
