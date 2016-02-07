def TYPE = {
    final MagicColor color ->
        new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
            @Override
            public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
                flags.removeAll();
                flags.add(color.getLandSubType());
            }
        }

        new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
            @Override
            public void modAbilityFlags(
                final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                final MagicManaType manaColor = color.getManaType();
                permanent.loseAllAbilities();
                permanent.addAbility(new MagicTapManaActivation(manaColor.getList(manaColor.getText())));
            }
        }
};

def action = {
    final MagicGame game, final MagicEvent event ->
        game.doAction(new AddStaticAction(event.getRefPermanent(), TYPE(event.getChosenColor())));
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Becomes"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_LAND,
                MagicDefaultTargetPicker.create(),
                this,
                "Target land\$ becomes the basic land type of PN's choice until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer(),
                    MagicColorChoice.ALL_INSTANCE,
                    it,
                    action,
                    "Chosen type\$."
                ));
            });
        }
    }
]
