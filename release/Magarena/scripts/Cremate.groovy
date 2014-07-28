[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Exile target card\$ from a graveyard. " +
                "Draw a card."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new MagicRemoveCardAction(
                    it,
                    MagicLocationType.Graveyard
                ));
                game.doAction(new MagicMoveCardAction(
                    it,
                    MagicLocationType.Graveyard,
                    MagicLocationType.Exile
                ));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
