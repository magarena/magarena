[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            // estimated number of cards in the graveyard. this may change
            // before resolution but we need to make a choice here
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicCondition.THRESHOLD_CONDITION.accept(cardOnStack) ?
                    MagicGraveyardTargetPicker.PutOntoBattlefield :
                    MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "Return target creature card\$ from your graveyard to your hand. " +
                "Return that card from your graveyard to the battlefield instead " +
                "if seven or more cards are in your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            event.processTargetCard(game, {
                if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                    game.doAction(new ReanimateAction(
                        it,
                        player
                    ));
                } else {
                    game.doAction(new MagicRemoveCardAction(it,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
