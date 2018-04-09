[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent) {
                final int amount = damage.replace();
                game.doAction(new ChangeCountersAction(permanent.getController(), permanent, MagicCounterType.MinusOne, amount));
            }
            return MagicEvent.NONE;
        }
    }
]
