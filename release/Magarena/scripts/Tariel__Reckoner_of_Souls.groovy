[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "PN chooses a creature card at random from target opponent's\$ graveyard. PN puts that card onto the battlefield under PN's control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList cards = new MagicCardList(CREATURE_CARD_FROM_GRAVEYARD.filter(it));
                for (final MagicCard card : cards.getRandomCards(1)) {
                    game.doAction(new ReanimateAction(
                        card,
                        event.getPlayer()
                    ));
                }
            });
        }
    }
]
