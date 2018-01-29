[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getSource().getController())) {
                damage.setAmount(2 * damage.getAmount());
            }
            return MagicEvent.NONE;
        }
    }
]

