[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction data) {
            return new MagicEvent(
                permanent,
                this,
                "Put SN on the top of your library."
            );
        }

       @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
		    final MagicCard card = event.getPermanent().getCard();
            if (card.isInGraveyard()) {
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.TopOfOwnersLibrary));
			}
        }
    }
]