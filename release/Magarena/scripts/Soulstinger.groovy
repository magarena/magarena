def action = {
    final int amount ->
    return {
        final MagicGame game, final MagicEvent event ->
        if (event.isYes()) {
            game.doAction(new ChangeCountersAction(event.getRefPermanent(), MagicCounterType.MinusOne, amount));
        }
    }
}

[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent diedPermanent) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE,
                permanent.getCounters(MagicCounterType.MinusOne),
                this,
                "PN may put a -1/-1 counters on target creature\$ for each -1/-1 counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice("Put -1/-1 counter?"),
                event.getTarget(),
                action(event.getRefInt()),
                "\$"
            ));
        }
    }
]

