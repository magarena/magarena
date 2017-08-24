[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Copy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{4}{G/W}{G/W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "For each creature token PN controls, he or she creates a token that's a copy of that creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_TOKEN_YOU_CONTROL.filter(event) each {
                game.doAction(new PlayTokenAction(event.getPlayer(), it));
            }
        }
    }
]
