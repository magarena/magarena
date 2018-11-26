[
    new MagicStatic(MagicLayer.ModPT, PERMANENT_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Skeleton) ||
                    (source != target && target.hasSubType(MagicSubType.Zombie));
        }
    },
    new MagicStatic(MagicLayer.Ability, PERMANENT_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Deathtouch, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Skeleton) ||
                    (source != target && target.hasSubType(MagicSubType.Zombie));
        }
    }
]
