[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Weaken"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                this,
                "Target creature\$ gets -X/-0 until end of turn, where X is the number of Merfolk and/or Faeries PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPlayer().getNrOfPermanents(
                    permanentOr(
                        MagicSubType.Merfolk,
                        MagicSubType.Faerie,
                        Control.You
                    )
                );
                game.doAction(new ChangeTurnPTAction(it,-amount,0));
            });
        }
    }
]
