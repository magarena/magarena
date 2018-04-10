[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget())) {
                final int amount = damage.replace();
                game.doAction(new ChangeCountersAction(permanent.getController(),permanent,MagicCounterType.Depletion,amount));
            }
            return MagicEvent.NONE;
        }
    }
]
