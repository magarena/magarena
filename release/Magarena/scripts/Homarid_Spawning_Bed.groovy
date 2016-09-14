[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Tokens"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{U}{U}"),
                new MagicSacrificePermanentEvent(source, new MagicTargetChoice("a blue creature to sacrifice"))
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "PN puts X 1/1 blue Camarid creature tokens onto the battlefield, where X is RN's converted mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=event.getRefPermanent().getConvertedCost();
            game.logAppendX(player,amount);
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("1/1 blue Camarid creature token"),
                amount
            ));
        }
    }
]
