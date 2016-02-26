[
    new LeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return (act.getPermanent() == enchanted) ?
                new MagicEvent(
                    permanent,
                    enchanted.getController(),
                    this,
                    "PN returns SN from its owner's graveyard to the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getPermanent().getCard();
            if (card.isInGraveyard()) {
                game.doAction(new ReturnCardAction(
                    MagicLocationType.Graveyard,
                    event.getPermanent().getCard(),
                    event.getPlayer()
                ));
            }
        }
    }
]
