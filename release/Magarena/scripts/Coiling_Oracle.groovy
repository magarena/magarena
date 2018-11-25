[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN reveals the top card of his or her library. If it's a land card, PN puts it onto the battlefield. " +
                "Otherwise, PN puts that card into his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new RevealAction(card));
                if (card.hasType(MagicType.Land)) {
                    game.doAction(new PutOntoBattlefieldAction(
                        MagicLocationType.OwnersLibrary,
                        card,
                        event.getPlayer()
                    ));
                } else {
                    game.doAction(new ShiftCardAction(
                        card,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.OwnersHand
                    ));
                }
            }
        }
    }
]
