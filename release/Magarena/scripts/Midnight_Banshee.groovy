[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a -1/-1 counter on each nonblack creature."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_NONBLACK_CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicChangeCountersAction(target,MagicCounterType.MinusOne,1,true));
            }
        }
    }
]
