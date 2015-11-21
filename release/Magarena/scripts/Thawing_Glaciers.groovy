[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a basic land card, puts that card onto the battlefield tapped, "+
                "then shuffles his or her library. Return SN to its owner's hand at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    BASIC_LAND_CARD_FROM_LIBRARY,
                    1, 
                    true, 
                    "to put onto the battlefield tapped"
                ),
                MagicPlayMod.TAPPED
            ));
            game.doAction(new AddTriggerAction(event.getPermanent(),AtEndOfTurnTrigger.Return))
        }
    }
]
