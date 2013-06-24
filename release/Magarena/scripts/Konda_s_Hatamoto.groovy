[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            final MagicGame game = source.getGame();
            if (game.filterPermanents(permanent.getController(), MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL).size() > 0) {
                flags.add(MagicAbility.Vigilance);
            }
        }
    },
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(permanent.getController(),MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL);
            if (targets.size() > 0) {
                pt.add(1,2);
            }
        }
    }
]
