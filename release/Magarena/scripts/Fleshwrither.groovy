def choice = new MagicTargetChoice(
    new MagicCMCCardFilter(CREATURE_CARD_FROM_LIBRARY,Operator.EQUAL,4),
    "a creature card with converted mana cost 4 from your library"
);

[
    new MagicPermanentActivation(
        [MagicCondition.SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Transfigure"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{B}{B}"),
                new MagicSacrificeEvent(source),
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a creature with the same converted mana cost as this creature " +
                "and puts that card onto the battlefield. Then shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(event,choice));
        }
    }
]
