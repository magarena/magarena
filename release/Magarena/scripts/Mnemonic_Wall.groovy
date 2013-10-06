[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD
                ),
                MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "PN may\$ return target instant or sorcery card\$ " +
                "from his or her graveyard to his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                    }
                });
            }
        }
    }
]
