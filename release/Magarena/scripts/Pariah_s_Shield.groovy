[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REDIRECT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) && permanent.getEquippedCreature().isValid()) {
                // Replacement effect. Generates no event or action.
                damage.setTarget(permanent.getEquippedCreature());
            }
            return MagicEvent.NONE;
        }
    }
]
