[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Main"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{3}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for a Elf card, reveal it, put it into his or her hand, and shuffle his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                new MagicMayChoice(
                    "Search for a Goblin card?",
                    MagicTargetChoice.ELF_CARD_FROM_LIBRARY
                )
            ));
        }
    }
]
