[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a 1/1 green Insect creature token with infect "+
                "for each poison counter PN's opponents have."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getOpponent().getPoison();
            game.logAppendValue(player, amount);
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("1/1 green Insect creature token with infect"),
                amount
            ));
        }
    }
]
