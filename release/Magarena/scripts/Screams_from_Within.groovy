[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
            return (died == permanent.getEnchantedPermanent()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN returns SN from his or her graveyard to the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getPermanent().getCard();
            if (card.isInGraveyard()) {
                game.doAction(new ReturnCardAction(
                    MagicLocationType.Graveyard,
                    card,
                    event.getPlayer()
                ));
            }
        }
    }
]
