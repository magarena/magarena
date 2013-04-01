[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getController().getNrOfPermanentsWithSubType(MagicSubType.Griffin) > 0) {
                pt.add(3,3);
            }
        }        
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().getNrOfPermanentsWithSubType(MagicSubType.Griffin) > 0) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
