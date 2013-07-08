[
    new MagicIfDamageWouldBeDealtTrigger(4) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return damage.isPreventable() &&
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
                    
            game.doAction(new MagicChangeCountersAction(
                damage.getTargetPermanent(),
                MagicCounterType.PlusOne,
                amount,
                true
            ));

            game.logMessage(
                permanent.getController(),
                "Prevent ${amount} damage and put ${amount} +1/+1 counters on ${damage.getTarget()}"
            );

            return MagicEvent.NONE;
        }
    }
]
