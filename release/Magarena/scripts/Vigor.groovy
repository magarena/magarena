[
    new MagicIfDamageWouldBeDealtTrigger(4) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return
                damage.isPreventable() &&
                damage.getAmount() > 0 &&
                target != permanent &&
                target.isCreature() &&
                permanent.isFriend(target);
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = damage.getAmount();

            // Prevention effect.
            damage.setAmount(0);

            return new MagicEvent(
                permanent,
                damage.getTarget(),
                {
                    final MagicGame G, final MagicEvent E ->
                    G.doAction(new MagicChangeCountersAction(
                        E.getRefPermanent(),
                        MagicCounterType.PlusOne,
                        amount,
                        true
                    ));
                } as MagicEventAction ,
                "Put "+amount+" +1/+1 counters on RN."
            );
        }
    }
]
