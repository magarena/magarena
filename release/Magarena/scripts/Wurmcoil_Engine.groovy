[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
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
            game.doAction(new MagicPlayTokenAction(
                player,
                TokenCardDefinitions.get("3/3 colorless Wurm artifact creature token with deathtouch")
            ));
            game.doAction(new MagicPlayTokenAction(
                player,
                TokenCardDefinitions.get("3/3 colorless Wurm artifact creature token with lifelink")
            ));
        }
    }
]
