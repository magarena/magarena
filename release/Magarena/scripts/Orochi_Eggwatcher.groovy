[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{G}"),
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a 1/1 green Snake creature token onto the battlefield. "+
                "If you control ten or more creatures, flip SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 green Snake creature token")
            ));
            if (event.getPlayer().getNrOfPermanents(MagicType.Creature) >= 10) {
                game.doAction(new FlipAction(event.getPermanent()));
            }
        }
    }
]
