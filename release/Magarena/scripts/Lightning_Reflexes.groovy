[
    new MagicHandCastActivation(
        [MagicCondition.NOT_SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Flash"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return source.getCostEvent();
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicCardDefinition cdef = cardOnStack.getCardDefinition();
            final MagicPlayAuraEvent auraEvent = (MagicPlayAuraEvent)cdef.getCardEvent();
            final MagicTargetChoice choice = auraEvent.getTargetChoice();
            return new MagicEvent(
                cardOnStack,
                choice,
                auraEvent.getTargetPicker(),
                this,
                "Enchant " + choice.getTargetDescription() + "\$ with SN. " +
                "Sacrifice SN at the beginning of the next cleanup step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new PlayCardFromStackAction(
                    event.getCardOnStack(),
                    it,
                    MagicPlayMod.SACRIFICE_AT_END_OF_TURN
                ));
            });
        }
    }
]
