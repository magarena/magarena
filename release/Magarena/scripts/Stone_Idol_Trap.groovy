[
     new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Token, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setCardAct(this);
        }

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final int n = source.getGame().getNrOfPermanents(ATTACKING_CREATURE);
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getCost().reduce(MagicCostManaType.Colorless, n)
                )
            ];
        }
    },
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a 6/12 colorless Construct artifact creature token with trample onto the battlefield. " + 
                "Exile it at the beginning of your next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("6/12 colorless Construct artifact creature token with trample"),
                [MagicPlayMod.EXILE_AT_END_OF_TURN]
            ));
        }
    }
]
