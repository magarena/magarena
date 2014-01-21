[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicColor.Red)) {
                flags.add(MagicAbility.FirstStrike);
            }
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (source.getController().controlsPermanent(MagicColor.Red)) {
                pt.add(1,0);
            }
        }
    }
    
]
