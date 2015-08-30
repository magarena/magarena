[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN puts the top X cards of his or her library into his or her graveyard, "+
                "where X is the number of cards in his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getHandSize()
            game.logAppendX(player,amount);
            game.doAction(new MillLibraryAction(player,amount));
        }
    }
]
