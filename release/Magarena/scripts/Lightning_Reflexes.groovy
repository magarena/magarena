[
    new MagicCardActivation(
        [MagicCondition.NOT_SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Flash"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return source.getCostEvent();
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicFirstStrikeTargetPicker.create(),
                this,
                "Enchant target creature\$ with SN. " +
                "Sacrifice SN at the beginning of the next cleanup step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack(),
                    it,
                    MagicPlayMod.SACRIFICE_AT_END_OF_TURN
                ));
            });
        }
    }
]
