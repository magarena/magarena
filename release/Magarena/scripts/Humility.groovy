[
    new MagicStatic(MagicLayer.Ability,MagicTargetFilterFactory.CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
        }
    },
    
    new MagicStatic(MagicLayer.SetPT,MagicTargetFilterFactory.CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(1,1);
        }
    }
]
