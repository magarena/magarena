def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
    } else {
        game.doAction(new DealDamageAction(event.getSource(),event.getRefPermanent(),1));
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 1 damage to target creature\$ unless that creature's controller has SN deal 2 damage to him or her."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice("have SN deal 2 damage to you?"),
                    it,
                    action,
                    "PN may\$ have SN deal 2 damage to him or her. If PN doesn't, SN deals 1 damage to RN."
                ));
            });
        }
    }
]
