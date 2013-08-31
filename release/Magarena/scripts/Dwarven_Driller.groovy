def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        final MagicDamage damage = new MagicDamage(
            event.getSource(),
            event.getPlayer(),
            2
        );
        game.doAction(new MagicDealDamageAction(damage));
    } else {
        game.doAction(new MagicDestroyAction(event.getRefPermanent()));
    }
} as MagicEventAction

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_LAND,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target land\$ unless its controller has " +
                "SN deal 2 damage to him or her."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice(),
                    permanent,
                    action,
                    "PN may\$ have SN deal 2 damage to him or her."
                ));
            } as MagicPermanentAction);
        }
    }
]
