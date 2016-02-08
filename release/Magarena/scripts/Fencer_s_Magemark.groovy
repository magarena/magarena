[
    new MagicStatic(MagicLayer.Ability, CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.FirstStrike,flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.isEnchanted();
        }
    },
    new MagicStatic(MagicLayer.ModPT, CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return target.isEnchanted();
        }
    }
]
