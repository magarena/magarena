[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().getNrOfPermanentsWithSubType(MagicSubType.Gate) > 0) {
		        flags.add(MagicAbility.Vigilance);
            }
        }
    }
]
