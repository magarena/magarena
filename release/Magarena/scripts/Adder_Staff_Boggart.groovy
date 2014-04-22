def clashAction = {
    final MagicGame game, final MagicEvent event ->
    if(event.getRefInt() == 1) {
        game.doAction(new MagicChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, 1, true));
    }
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicClashEvent(permanent, permanent.getController(), clashAction);
        }
    }
]
