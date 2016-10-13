[
    new ThisBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return new MagicEvent(
                permanent,
                blocked,
                this,
                "PN creates a 1/1 white Cat Soldier creature token with vigilance that's blocking RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final rn = event.getRefPermanent();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 white Cat Soldier creature token with vigilance"),
                {
                    final MagicPermanent perm ->
                    final MagicGame G = perm.getGame();
                    G.doAction(new SetBlockerAction(rn.map(G), perm));
                }
            ));
        }
    }
]
