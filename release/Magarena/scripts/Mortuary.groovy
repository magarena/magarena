[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return (otherPermanent.isNonToken() &&
                otherPermanent.isCreature() &&
                otherPermanent.isOwner(permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN puts RN on the top of his or her library."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getRefPermanent().getCard();
            if (card.getLocation() == MagicLocationType.Graveyard) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.Graveyard,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            }
        }
    }
]
