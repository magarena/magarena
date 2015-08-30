def LAND_OPPONENT_CONTROLS = MagicTargetFilterFactory.permanent(MagicType.Land, Control.Opp);

[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            final List<MagicManaType> manaTypes = new ArrayList<MagicManaType>();
            for (final MagicManaType coloredManaType : MagicManaType.ALL_COLORS) { //Color first to prevent duplicate colours. Can't use Set as MagicManaActivation requires List
                for (final MagicPermanent land : LAND_OPPONENT_CONTROLS.filter(source)) {
                    for (final MagicManaActivation manaAbility : land.getManaActivations()) {
                        for (final MagicManaType generates : manaAbility.getManaTypes()){
                            if (coloredManaType == generates) {
                                manaTypes.add(coloredManaType);
                            }
                        }
                    }
                }
            }
            for (final MagicManaType mana : manaTypes) {
                final MagicManaActivation landManaAbility = MagicManaActivation.create("{T}", manaTypes);
                permanent.addAbility(landManaAbility);
            }
        }
    }
]
//Too much scanning of the battlefield to determine types. 
