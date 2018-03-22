[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent &&
                damage.isCombat() &&
                damage.getSource().hasType(MagicType.Creature) &&
                permanent.getBlockingCreatures().contains(damage.getSource())) {
                    // Replacement effect. Generates no event or action.
                    damage.prevent();
                }
            return MagicEvent.NONE;
        }
    }
]
