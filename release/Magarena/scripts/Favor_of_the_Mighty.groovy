[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_CREATURE) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            final MagicPlayer player = permanent.getController();
            final MagicGame game = player.getGame();
            final Collection<MagicPermanent> targets = game.filterPermanents(
                    player,
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            final Collection<MagicPermanent> targets2 = game.filterPermanents(
                    player.getOpponent(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            int highest = 0;
            for (final MagicPermanent creature : targets) {
                highest = Math.max(highest,creature.getConvertedCost());
            }
            for (final MagicPermanent creature : targets2) {
                highest = Math.max(highest,creature.getConvertedCost());
            }
            
            if (permanent.getConvertedCost() == highest) {
                flags.add(MagicAbility.ProtectionFromAllColors);
            }
        }
    }
]
