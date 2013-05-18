[
    new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent died) {
            final MagicPermanent enchanted = permanent.getEnchantedCreature();
            return (enchanted == died) ?
                new MagicEvent(
                    permanent,
                    enchanted.getCard(),
                    this,
                    "Return RN to the battlefield under your control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getRefCard();
            if (card.getOwner().getGraveyard().contains(card)) {
                game.doAction(new MagicReanimateAction(
                    event.getPlayer(),
                    card,
                    MagicPlayCardAction.NONE
                ));
            }
        }
    }
]
