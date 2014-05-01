[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Equipment"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{W}"),
                new MagicTapEvent(source),
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicPumpTargetPicker.create(),
                this,
                "PN searches PN's library for an equipment card and puts it onto the battlefield. Attaches it to a creature PN controls. Then shuffles PN's library."
            );
      }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                final MagicSearchOntoBattlefieldEvent eventAction = new MagicSearchOntoBattlefieldEvent(
                    event,
                    MagicTargetChoice.EQUIPMENT_CARD_FROM_LIBRARY
                );
                game.addEvent(eventAction);
                final MagicPermanent equipment = eventAction.getPermanent();
                game.doAction(new MagicAttachAction(equipment, permanent));
            });
        }
    }
]
