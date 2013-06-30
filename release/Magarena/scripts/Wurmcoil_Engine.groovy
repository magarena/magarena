[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts " + 
                "a 3/3 colorless Wurm artifact creature token with deathtouch and "+
                "a 3/3 colorless Wurm artifact creature token with lifelink onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Wurm1")));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Wurm2")));
        }
    }
]
