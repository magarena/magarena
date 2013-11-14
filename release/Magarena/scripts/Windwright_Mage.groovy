def LEAST_ONE_ARTIFACT_IN_GRAVEYARD = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicGame game = source.getGame();
        final MagicPlayer player = source.getController();
        return game.filterCards(player, MagicTargetFilter.TARGET_ARTIFACT_CARD_FROM_GRAVEYARD ).size() >= 1;
    }
};
[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (LEAST_ONE_ARTIFACT_IN_GRAVEYARD.accept(permanent)) {
                flags.add(MagicAbility.Flying);
            }
        }
    }
]
