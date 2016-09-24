[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts an X/X green Elemental creature token onto the battlefield, where X is the number of creature cards in PN's graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
            game.logAppendValue(event.getPlayer(),x);
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("green Elemental creature token"),
                MagicPlayMod.PT(x,x)
            ));
        }
    }
]
