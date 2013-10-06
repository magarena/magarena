[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "Put target card\$ from your graveyard on top of your library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(
                        targetCard,
                        MagicLocationType.Graveyard,
                        MagicLocationType.TopOfOwnersLibrary
                    ));
                }
            });
        }
    }
]
