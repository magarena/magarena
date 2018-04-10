[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = (damage.getTarget() == permanent) ? damage.prevent() : 0;
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "Prevent RN damage and put RN -1/-1 counters on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPlayer(),
                event.getPermanent(),
                MagicCounterType.MinusOne,
                event.getRefInt()
            ));
        }
    }
]
