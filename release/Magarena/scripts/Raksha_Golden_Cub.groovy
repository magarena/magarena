[
    new MagicStatic(
        MagicLayer.ModPT,
        CAT_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(2,2);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.isEquipped();
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        CAT_CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.DoubleStrike, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.isEquipped();
        }
    }
]
