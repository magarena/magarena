[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            new MagicEvent(
                permanent,
                permanent.getOwner(),
                new MagicMayChoice(),
                this,
                "PN may\$ return SN to his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getPermanent().getCard()
            if (event.isYes() && card.isInGraveyard()) {
                game.doAction(new ShiftCardAction(card, MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
            }
        }
    }
]
