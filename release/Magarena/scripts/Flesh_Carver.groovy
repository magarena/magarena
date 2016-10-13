[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                permanent.getPower(),
                this,
                "PN creates an X/X black Horror creature token, where X is SN's power. (RN)"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getRefInt();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken(x, x, "black Horror creature token")
            ));
        }
    }
]
