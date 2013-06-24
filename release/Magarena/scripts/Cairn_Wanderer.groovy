def CAIRN_WANDERER_FLAGS = MagicAbility.of(
    MagicAbility.Flying,
    MagicAbility.Fear,
    MagicAbility.FirstStrike,
    MagicAbility.DoubleStrike,
    MagicAbility.Deathtouch,
    MagicAbility.Haste,
    MagicAbility.Lifelink,
    MagicAbility.Reach,
    MagicAbility.Trample,
    MagicAbility.Shroud,
    MagicAbility.Vigilance
);

CAIRN_WANDERER_FLAGS.addAll(MagicAbility.LANDWALK_FLAGS);
CAIRN_WANDERER_FLAGS.addAll(MagicAbility.PROTECTION_FLAGS);

[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            final MagicGame game = source.getGame();
            for (final MagicPlayer player : game.getPlayers()) {
                for (final MagicCard card : player.getGraveyard()) {
                    final MagicCardDefinition cardDefinition = card.getCardDefinition();
                    if (cardDefinition.isCreature()) {
                        final Set<MagicAbility> creatureAbilities = cardDefinition.genAbilityFlags();
                        creatureAbilities.retainAll(CAIRN_WANDERER_FLAGS);
                        flags.addAll(creatureAbilities);
                    }
                }
            }
        }
    }
]
