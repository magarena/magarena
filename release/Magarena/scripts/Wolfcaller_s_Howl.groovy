[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getOpponent().getHandSize() >= 4 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN creates X 2/2 green Wolf creature tokens, where X is the number of your opponents with four or more cards in hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("2/2 green Wolf creature token"), 1));
        }
    }
]
