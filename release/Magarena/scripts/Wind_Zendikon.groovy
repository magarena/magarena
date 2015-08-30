[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return (enchanted == died) ?
                new MagicEvent(
                    permanent,
                    enchanted.getCard(),
                    this,
                    "Return RN to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(
                event.getRefCard(),
                MagicLocationType.Graveyard,
                MagicLocationType.OwnersHand
            ));
        }
    }
]
