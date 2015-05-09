[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN, then shuffle all creature cards from PN's graveyard into his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getPermanent().getCard();
            game.doAction(new RemoveCardAction(card, MagicLocationType.Graveyard));
            game.doAction(new MoveCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
            CREATURE_CARD_FROM_GRAVEYARD.filter(event) each {
                game.doAction(new RemoveCardAction(it, MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(it, MagicLocationType.Graveyard, MagicLocationType.OwnersLibrary));
            }
        }
    }
]
