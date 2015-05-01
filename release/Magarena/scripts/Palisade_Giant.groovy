[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (permanent.isController(damage.getTarget()) || permanent.getController() == damage.getTarget().getController()){
                // Replacement effect. Generates no event or action.
                damage.setTarget(permanent);
            }
            return MagicEvent.NONE;
        }
    }
]
