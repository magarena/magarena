[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            int amount = 0;

            if (target != permanent &&
                target.isCreaturePermanent() &&
                permanent.isFriend(target)) {
                amount = damage.prevent();
            }

            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    {
                        final MagicGame G, final MagicEvent E ->
                        G.doAction(new ChangeCountersAction(
                            E.getRefPermanent(),
                            MagicCounterType.PlusOne,
                            amount
                        ));
                    },
                    "Prevent ${amount} damage and put ${amount} +1/+1 counters on RN"
                ):
                MagicEvent.NONE;
        }
    }
]
