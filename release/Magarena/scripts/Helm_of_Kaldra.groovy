def HAS_ALL_KALDRA_EQUIPMENT = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicTargetFilter<MagicPermanent> shield = new MagicNameTargetFilter("Shield of Kaldra");
        final MagicTargetFilter<MagicPermanent> sword = new MagicNameTargetFilter("Sword of Kaldra");
        final MagicTargetFilter<MagicPermanent> helm = new MagicNameTargetFilter("Helm of Kaldra");
        final MagicPlayer player = source.getController();
        return player.controlsPermanent(shield) &&
               player.controlsPermanent(sword) &&
               player.controlsPermanent(helm);
    }
};

[
    new MagicPermanentActivation(
        [HAS_ALL_KALDRA_EQUIPMENT],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{1}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a legendary 4/4 colorless Avatar creature token named Kaldra onto the battlefield and attach those Equipment to it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int pIdx = event.getPlayer().getIndex();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("legendary 4/4 colorless Avatar creature token named Kaldra"),
                {
                    final MagicPermanent perm ->
                    KALDRA_EQUIPMENT_YOU_CONTROL.filter(perm) each {
                        perm.getGame().doAction(new AttachAction(it, perm));
                    }
                }
            ));
        }
    }
]
