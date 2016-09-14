[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{B}{G}"),
                new MagicSacrificePermanentEvent(source, new MagicTargetChoice("a nontoken creature to sacrifice"))
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getController(),
                payedCost.getTarget(),
                this,
                "PN puts X 1/1 green Saproling creature tokens onto the battlefield, where X is the sacrificed creature\$'s toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent sacrificed = event.getRefPermanent();
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("1/1 green Saproling creature token"), sacrificed.getToughness()));
        }
    }
]
