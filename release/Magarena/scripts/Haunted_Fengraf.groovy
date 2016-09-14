[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.None),
        "Return"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source),
                    new MagicPayManaCostEvent(source,"{3}"),
                    new MagicSacrificeEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN returns a creature card at random from his or her graveyard to his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = new MagicCardList(CREATURE_CARD_FROM_GRAVEYARD.filter(event));
            for (final MagicCard card : cards.getRandomCards(1)) {
                game.doAction(new ShiftCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                game.logAppendMessage(player, "${player.getName()} returns (${card.getName()}) to his or her hand.");
            }
        }
    }
]
