[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a 0/1 green Plant creature token onto " +
                "the battlefield for each land he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            int amount = player.getNrOfPermanents(MagicType.Land);
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("0/1 green Plant creature token"),
                amount
            ));
        }
    }
]
