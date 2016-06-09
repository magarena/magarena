[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            if (permanent.getExiledCards().size() > 0) {
                final MagicCard exiled = permanent.getExiledCard();
                for (MagicColor color : MagicColor.values()) {
                    if (exiled.hasColor(color)) {
                        permanent.addAbility(new MagicTapManaActivation(Collections.singletonList(color.getManaType())));
                    }
                }
            }
        }
    }
]
