def TARGET_EQUIPMENT_YOU_CONTROL = new MagicTargetChoice("target Equipment you control");
        
[
    new MagicPermanentActivation(
        [MagicCondition.SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Attach"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{0}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_EQUIPMENT_YOU_CONTROL,
                this,
                "PN attaches target Equipment he or she controls\$ to SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AttachAction(it, event.getPermanent()));
            });
        }
    }
]
