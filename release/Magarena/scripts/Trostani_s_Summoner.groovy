[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a 2/2 white Knight creature token with vigilance, " +
                "a 3/3 green Centaur creature token, and " +
                "a 4/4 green Rhino creature token with trample."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("2/2 white Knight creature token with vigilance")
            ));
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("3/3 green Centaur creature token")
            ));
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("4/4 green Rhino creature token with trample")
            ));
        }
    }
]
