[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.ProtectionFromBlack, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasColor(MagicColor.Black);
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.ProtectionFromBlue, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasColor(MagicColor.Blue);
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.ProtectionFromGreen, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasColor(MagicColor.Green);
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.ProtectionFromRed, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasColor(MagicColor.Red);
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilterFactory.CREATURE
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.ProtectionFromWhite, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasColor(MagicColor.White);
        }
    }
]
