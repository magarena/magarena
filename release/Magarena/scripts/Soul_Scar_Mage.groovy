[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicDamage damage) {
            if (!damage.isCombat() &&
                damage.isTargetCreature() &&
                damage.getTargetPermanent().isController(permanent.getOpponent())) {
                
                final int amount = damage.replace();
                game.doAction(new ChangeCountersAction(damage.getTargetPermanent(), MagicCounterType.MinusOne, amount));
            }
            return MagicEvent.NONE;
        }
    }
]

