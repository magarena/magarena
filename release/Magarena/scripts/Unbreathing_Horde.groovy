[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            if (damage.getTarget() == permanent) {
                damage.prevent();
                game.doAction(new ChangeCountersAction(permanent, MagicCounterType.PlusOne,-1));
            }
            return MagicEvent.NONE;
        }
    }
]
