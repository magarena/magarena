[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Look at the top card of PN's library. If an instant or sorcery card is revealed this way, transform SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                      MagicLocationType.TopOfOwnersLibrary
                ));
            if (card.hasType(MagicType.Instant) || card.hasType(MagicType.Sorcery)) {
            game.doAction(new MagicTransformAction(event.getPermanent()));
            }
            }
        }
    }
]
