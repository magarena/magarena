[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent.getEnchantedPermanent() == died) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Return SN to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(
                event.getPermanent().getCard(),
                MagicLocationType.Graveyard,
                MagicLocationType.OwnersHand
            ));
        }
    }
]
