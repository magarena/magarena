[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicCard diedCard = died.getCard();
            return (permanent.getEnchantedPermanent() == died) ?
                new MagicEvent(
                    permanent,
                    diedCard,
                    this,
                    "Return RN to the battlefield under its owner's control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard died = event.getRefCard();
            game.doAction(new PutOntoBattlefieldAction(
                MagicLocationType.Graveyard,
                died,
                died.getOwner()
            ));
        }
    }
]
