[
    new MagicStatic(MagicLayer.AbilityCond) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Haste);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return game.getPlayers().any({
                final MagicPlayer player ->
                player.getPermanents().any({
                    final MagicPermanent permanent ->
                    permanent.hasType(MagicType.Creature) && permanent.getCounters(MagicCounterType.MinusOne) > 0;
                });
            });
        }
    }
]

