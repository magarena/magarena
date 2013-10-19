[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction data) {
            return new MagicEvent(
                permanent,
                this,
                "Return SN to its owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getPermanent().getCard();
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
        }
    }
]
