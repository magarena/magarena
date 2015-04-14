[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ put SN on the top of his or her library."
            );
        }

       @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                final MagicCard card = event.getPermanent().getCard();
                if (card.isInGraveyard()) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.TopOfOwnersLibrary));
                }
            }
        }
    }
]
