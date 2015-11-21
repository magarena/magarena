[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().isFriend(permanent) && MagicCondition.HELLBENT.accept(damage.getSource())) {
                damage.setAmount(damage.getAmount() * 2);
            }
            return MagicEvent.NONE;
        }
    }
]
