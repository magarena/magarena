def trigger = {
    final MagicTargetFilter filter ->
    return new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer) &&
                    game.filterPermanents(
                        upkeepPlayer,
                        filter
                    ).size() > 0) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    1,
                    true
                ));
            }
        }
    };
}
[
    trigger(MagicTargetFilter.TARGET_BLACK_PERMANENT_YOU_CONTROL),
    trigger(MagicTargetFilter.TARGET_GREEN_PERMANENT_YOU_CONTROL)
]
