[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates X 2/2 black Zombie creature tokens, " +
                "where X is half the number of Zombies he or she controls, rounded down"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("2/2 black Zombie creature token"),
                player.getNrOfPermanents(MagicSubType.Zombie).intdiv(2)
            ));
        }
    }
]
