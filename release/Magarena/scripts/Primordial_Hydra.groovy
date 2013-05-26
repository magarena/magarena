[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne);
            if (amount >= 10) {
                flags.add(MagicAbility.Trample);
            }
        }
    },
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne);
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "Put RN +1/+1 counters on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                event.getRefInt(),
                true
            ));
        }
    }
]
