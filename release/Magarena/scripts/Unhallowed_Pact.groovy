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
                    "Return RN to the battlefield under your control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getRefCard();
            if (card.isInGraveyard()) {
                game.doAction(new ReanimateAction(
                    card,
                    event.getPlayer()
                ));
            }
        }
    }
]
