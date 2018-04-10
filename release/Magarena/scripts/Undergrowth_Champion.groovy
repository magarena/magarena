[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent && permanent.hasCounters(MagicCounterType.PlusOne)) {
                damage.prevent();
                game.doAction(new ChangeCountersAction(permanent.getController(), permanent, MagicCounterType.PlusOne,-1));
            }
            return MagicEvent.NONE;
        }
    }
]
