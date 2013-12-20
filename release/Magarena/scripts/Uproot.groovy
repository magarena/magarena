[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_LAND,
                this,
                "Put target land\$ on top of its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard targetCard ->
                game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Play));
                game.doAction(new MagicMoveCardAction(
                    targetCard,
                    MagicLocationType.Play,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            });
        }
    }
]
