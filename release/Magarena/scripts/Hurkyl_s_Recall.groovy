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
                ARTIFACT_YOU_OWN.filter(it) each {
                    final MagicPermanent artifact ->
                    game.doAction(new MagicRemoveFromPlayAction(artifact,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
