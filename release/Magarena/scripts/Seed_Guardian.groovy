[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates an X/X green Elemental creature token, where X is the number of creature cards in PN's graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
            game.logAppendValue(event.getPlayer(),x);
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken(x, x, "green Elemental creature token")
            ));
        }
    }
]
