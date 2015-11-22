[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all creature cards from PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_CARD_FROM_GRAVEYARD.filter(event) each {
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.Exile));
            }
        }
    }
]
