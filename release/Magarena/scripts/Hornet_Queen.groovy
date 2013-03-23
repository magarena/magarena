[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    this,
                    "PN puts four 1/1 green Insect creature tokens with flying and deathtouch onto the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            for (int i = 0 ; i < 4; i++) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("Insect3")));
            }
        }
    }
]
