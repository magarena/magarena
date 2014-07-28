[
    new MagicPermanentActivation(
        [MagicCondition.FACE_DOWN_CONDITION]
        new MagicActivationHints(MagicTiming.Pump),
        "Morph"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN turns SN face up."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            //NO_EVENT
        }
    },
    
    new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Morph"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }
    }
]
