[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token,true),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{3}{R}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a 2/1 red Goblin creature token with haste onto the battlefield. Exile it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            final MagicCard card = MagicCard.createTokenCard(TokenCardDefinitions.get("2/1 red Goblin creature token with haste"),player);
            game.doAction(new MagicPlayCardAction(
                card,
                player,
                [MagicPlayMod.EXILE_AT_END_OF_TURN]
            ));
        }
    }
]
