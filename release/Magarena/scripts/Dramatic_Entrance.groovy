[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_GREEN_CREATURE_CARD_FROM_HAND,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Put a green creature card\$ from your hand onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                    game.doAction(new MagicPlayCardAction(card,event.getPlayer()));
                }
            });
        }
    }
]
