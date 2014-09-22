def FEWER_CREATURES_CONDITION = new MagicCondition() {
        public boolean accept(final MagicSource source) {
            return source.getController().getNrOfPermanents(MagicType.Creature) < source.getOpponent().getNrOfPermanents(MagicType.Creature);
        }
    };
[
    new MagicCardActivation(
        [FEWER_CREATURES_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Token),
        "Tokens"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setCardAct(this);
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put three 1/1 white Soldier creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 white Soldier creature token"),
                3
            ));   
        }
    }
]
