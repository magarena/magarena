[
    new MagicStatic(MagicLayer.SetPT,LAND_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(2,2);
        }
    },
    new MagicStatic(MagicLayer.Type,LAND_YOU_CONTROL) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    },
    new MagicStatic(MagicLayer.Ability,LAND_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.FirstStrike, flags);
        }
    }
]
