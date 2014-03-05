[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int lands = permanent.getController().getNrOfPermanents(MagicType.Land);
            if (lands>=8) {
                pt.add(4,4);
            }
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            final int lands = permanent.getController().getNrOfPermanents(MagicType.Land);
            if (lands>=8) {
                permanent.addAbility(MagicAbility.Trample, flags);
            }
        }
    }
]
