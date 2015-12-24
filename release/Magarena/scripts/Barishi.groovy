[
    new ThisDiesTrigger() {
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
            game.doAction(new ShiftCardAction(
                event.getPermanent().getCard(),
                MagicLocationType.Graveyard,
                MagicLocationType.Exile
            ));
            game.doAction(new ShuffleCardsIntoLibraryAction(
                CREATURE_CARD_FROM_GRAVEYARD.filter(event),
                MagicLocationType.Graveyard
            ));
        }
    }
]
