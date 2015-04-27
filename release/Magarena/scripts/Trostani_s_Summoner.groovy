[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 2/2 white Knight creature token with vigilance, " +
                "a 3/3 green Centaur creature token, and " +
                "a 4/4 green Rhino creature token with trample onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("2/2 white Knight creature token with vigilance")
            ));
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("3/3 green Centaur creature token")
            ));
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("4/4 green Rhino creature token with trample")
            ));
        }
    }
]
