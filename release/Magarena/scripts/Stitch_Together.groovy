[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            // estimated number of cards in the graveyard. this may change
            // before resolution but we need to make a choice here
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                new MagicGraveyardTargetPicker(MagicCondition.THRESHOLD_CONDITION.accept(cardOnStack)),
                this,
                "Return target creature card\$ from your graveyard to your hand. " +
                "Return that card from your graveyard to the battlefield instead " +
                "if seven or more cards are in your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                        game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
                    } else {
                        game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(targetCard,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                    }
                }
            });
        }
    }
]
