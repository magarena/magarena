def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicTuple tup = event.getRefTuple();
    game.doAction(new ChangeCountersAction(
        event.getPermanent(),
        MagicCounterType.PlusOne,
        tup.getInt(0)
    ));
    game.doAction(new MillLibraryAction(
        tup.getPlayer(1),
        tup.getInt(0)
    ));
}

[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            int amount = (damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer()) ? damage.replace() : 0;

            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    new MagicTuple(amount, damage.getTarget()),
                    action,
                    "Put ${amount} +1/+1 counters on SN and ${damage.getTarget()} puts that many cards from the top of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
    }
]
