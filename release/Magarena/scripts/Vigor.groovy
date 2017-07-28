def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicTuple tup = event.getRefTuple();
    game.doAction(new ChangeCountersAction(
        tup.getPermanent(1),
        MagicCounterType.PlusOne,
        tup.getInt(0)
    ));
}

[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            int amount = (target != permanent && target.isCreaturePermanent() && permanent.isFriend(target)) ? damage.prevent() : 0

            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    new MagicTuple(amount, damage.getTarget()),
                    action,
                    "Prevent ${amount} damage and put ${amount} +1/+1 counters on ${damage.getTarget()}."
                ):
                MagicEvent.NONE;
        }
    }
]
