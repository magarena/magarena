[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_CREATURE_PLUSONE_COUNTER),
                this,
                "PN may\$ move a +1/+1 counter from " +
                "target creature\$ onto SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    if (it.getCounters(MagicCounterType.PlusOne) > 0) {
                        game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1));
                        game.doAction(new ChangeCountersAction(it,MagicCounterType.PlusOne,-1));
                    }
                });
            }
        }
    }
]
