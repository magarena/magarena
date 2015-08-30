[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Switch"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Switch each creature's power and toughness until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                game.doAction(new AddStaticAction(it,MagicStatic.SwitchPT));
            }
        }
    }
]
