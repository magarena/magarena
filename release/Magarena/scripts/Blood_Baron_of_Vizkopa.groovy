[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getController().getLife() >= 30 &&
                permanent.getOpponent().getLife() <= 10) {
                pt.add(6,6);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> abilities) {
            if (permanent.getController().getLife() >= 30 &&
                permanent.getOpponent().getLife() <= 10) {
                abilities.add(MagicAbility.Flying);
            }
        }
    }
]
