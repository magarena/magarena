[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "token"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "4G"),
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates an X/X colorless Construct artifact creature token, " +()
                "where X is the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getPlayer().getPermanents().count({ it.hasType(MagicType.Creature) });
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken(x, x, "colorless Construct artifact creature token")
            ));
        }
    }
]

