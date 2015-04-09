def ARTIFACT_YOU_OWN = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isOwner(player) && target.isArtifact();
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Return all artifacts target player\$ owns to his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final Collection<MagicPermanent> targets = game.filterPermanents(it, ARTIFACT_YOU_OWN);
                for (final MagicPermanent permanent : targets) {
                    game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
