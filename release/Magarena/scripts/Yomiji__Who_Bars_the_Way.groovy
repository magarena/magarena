[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent != died && died.hasType(MagicType.Legendary)) ?
                new MagicEvent(
                    permanent,
                    died.getCard(),
                    this,
                    "Returns RN to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
        }
    }
]
