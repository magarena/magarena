[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().hasColor(MagicColor.Red) && damage.getTarget().isPlayer()) {
                damage.setAmount(damage.getAmount() +1);
            }
            return MagicEvent.NONE;
        }
    }
]
