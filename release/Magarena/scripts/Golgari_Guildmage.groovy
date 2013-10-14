[
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.FirstMain),
        "Return"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{4}{B}"),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_CREATURE)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "Return target creature card\$ from your graveyard to your hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {           
			event.processTargetCard(game,new MagicCardAction() {
				public void doAction(final MagicCard card) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
				}
			});
        }
    },
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{4}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "PN puts one +1/+1 counter on target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeCountersAction(
                        creature,
                        MagicCounterType.PlusOne,
                        1,
                        true
                    ));
                }
            });
        }
    }
]
