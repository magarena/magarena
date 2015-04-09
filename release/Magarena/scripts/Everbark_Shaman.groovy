def choice = new MagicTargetChoice("a Treefolk card from your graveyard");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicExileCardEvent(source, choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for two Forest cards, "+
                "and puts them onto the battlefield tapped. Then PN shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    MagicTargetFilterFactory.FOREST_CARD_FROM_LIBRARY,
                    2, 
                    true, 
                    "to put onto the battlefield tapped"
                ),
                MagicPlayMod.TAPPED
            ));
        }
    }
]
