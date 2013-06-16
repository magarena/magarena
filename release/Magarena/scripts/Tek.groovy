[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().controlsPermanent(MagicSubType.Plains)) {
                pt.add(0,2);
            }
        }
    },

    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (source.getController().controlsPermanent(MagicSubType.Swamp)) {
                pt.add(2,0);
            }
        }
    },

    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicSubType.Island)) {
                flags.add(MagicAbility.Flying);
            }
        }
    },

    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicSubType.Mountain)) {
                flags.add(MagicAbility.FirstStrike);
            }
        }
    },

    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicSubType.Forest)) {
                flags.add(MagicAbility.Trample);
            }
        }
    }
]
