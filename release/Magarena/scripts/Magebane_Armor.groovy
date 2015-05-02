[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.remove(MagicAbility.Flying);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) { 
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (!damage.isCombat() && damage.isTargetCreature()) {
                if (damage.getTargetPermanent() == permanent.getEquippedCreature()) {
                    // Prevention effect.
                    damage.prevent();
                }
            }
            return MagicEvent.NONE;
        }
    }
]
