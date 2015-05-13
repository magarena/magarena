def ARTIFACT_YOU_OWN = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
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
                game.doAction(new RemoveAllFromPlayAction(
                    ARTIFACT_YOU_OWN.filter(it),
                    MagicLocationType.OwnersHand
                ));
            });
        }
    }
]
