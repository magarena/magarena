[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ loses 1 life for each tapped artifact he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int artifacts = it.getNrOfPermanents(TAPPED_ARTIFACT_YOU_CONTROL);
                game.doAction(new ChangeLifeAction(it,artifacts));
            });
        }
    }
]
