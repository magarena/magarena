[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{G}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Exile target card\$ from a graveyard. " +
                "If it was a creature card, put a +1/+1 counter on Scavenging Ooze and you gain 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new ShiftCardAction(
                    it,
                    MagicLocationType.Graveyard,
                    MagicLocationType.Exile
                ));
                if (it.hasType(MagicType.Creature)) {
                    game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.PlusOne,1));
                    game.doAction(new ChangeLifeAction(event.getPlayer(),1));
                }
            });
        }
    }
]
