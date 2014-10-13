[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = source.getController().getNrOfPermanents(MagicSubType.Merfolk) + source.getController().getNrOfPermanents(MagicSubType.Faerie);
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                amount,
                this,
                "Target creature\$ gets -X/-0 until end of turn. (X="+amount+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefInt();
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,-amount,0));
            });
        }
    }
]
