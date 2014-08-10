[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer) &&
                    permanent.getCounters(MagicCounterType.Arrowhead) == 0) ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN.") :
                MagicEvent.NONE;
        }
    }
]
