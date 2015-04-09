[
    new MagicStatic(
        MagicLayer.ModPT,
        CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            if (permanent.getController().getNrOfPermanents(MagicType.Creature) == 1) {
                pt.add(3,1);
            }
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            if (permanent.getController().getNrOfPermanents(MagicType.Creature) == 1) {
                permanent.addAbility(MagicAbility.Lifelink, flags);
            }
        }
    }
]
