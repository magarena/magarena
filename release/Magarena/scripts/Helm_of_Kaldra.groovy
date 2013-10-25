def HAS_ALL_KALDRA_EQUIPMENT = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicTargetFilter<MagicPermanent> shield = new MagicTargetFilter.NameTargetFilter("Shield of Kaldra");
        final MagicTargetFilter<MagicPermanent> sword = new MagicTargetFilter.NameTargetFilter("Sword of Kaldra");
        final MagicTargetFilter<MagicPermanent> helm = new MagicTargetFilter.NameTargetFilter("Helm of Kaldra");
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
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
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
            final MagicPutIntoPlayAction action = new MagicPlayTokenAction(
                event.getPlayer(), 
                TokenCardDefinitions.get("legendary 4/4 colorless Avatar creature token named Kaldra")
            );
            game.doAction(action);
            final MagicPermanent kaldra = action.getPermanent();
            for (final equip : game.filterPermanents(event.getPlayer(), MagicTargetFilter.TARGET_KALDRA_EQUIPMENT)) {
                game.doAction(new MagicAttachAction(equip, kaldra));
            }
        }
    }
]
