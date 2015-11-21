[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isTargetPlayer()) {
                if (damage.getTargetPlayer() == permanent.getController() && 
                   (damage.getSource().hasType(MagicType.Sorcery) || damage.getSource().hasType(MagicType.Instant)) &&
                    damage.getAmount() >= 3) {
                    damage.setAmount(2);
                }
            }
            return MagicEvent.NONE;
        }
    }
]
