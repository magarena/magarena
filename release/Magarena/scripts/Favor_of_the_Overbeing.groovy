[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1, 1);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Green);
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Vigilance, flags);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Green);
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1, 1);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Blue);
        }
    },
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Flying, flags);
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target) && target.hasColor(MagicColor.Blue);
        }
    }
]
