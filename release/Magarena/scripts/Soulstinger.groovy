[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent diedPermanent) {
            final int amount = permanent.getCounters(MagicCounterType.MinusOne);
            return new MagicEvent(
                permanent,
                new MagicMayChoice(NEG_TARGET_CREATURE),
                amount,
                this,
                "PN may\$ put ${amount} -1/-1 counter(s) on target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ChangeCountersAction(event.getPlayer(), it, MagicCounterType.MinusOne, event.getRefInt()));
                });
            }
        }
    }
]

