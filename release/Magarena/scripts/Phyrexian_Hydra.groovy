[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            int amount = 0;

            if (damage.getTarget() == permanent) {
                amount = damage.prevent();
            }

            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    {
                        final MagicGame G, final MagicEvent E ->
                        G.doAction(new ChangeCountersAction(
                            E.getPermanent(),
                            MagicCounterType.MinusOne,
                            amount
                        ));
                    },
                    "Prevent ${amount} damage and put ${amount} -1/-1 counters on SN"
                ):
                MagicEvent.NONE;
        }
    }
]
