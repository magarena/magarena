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
                "PN returns target creature card from his or her graveyard\$ to his or her hand. " +
                "PN returns that card from his or her graveyard to the battlefield instead " +
                "if seven or more cards are in his or her graveyard."
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
                    game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
