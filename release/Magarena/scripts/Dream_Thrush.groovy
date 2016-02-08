def TYPE = {
    final MagicColor color ->
    return new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.removeAll();
            flags.add(color.getLandSubType());
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent, final int flags) {
            return MagicType.Basic.getMask() | MagicType.Land.getMask();
        }
    }
};

def MANA = {
    final MagicColor color ->
    return new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.loseAllAbilities();
            permanent.addAbility(new MagicTapManaActivation(Collections.singletonList(color.getManaType())));
        }
    }
};

def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new AddStaticAction(event.getRefPermanent(), TYPE(event.getChosenColor())));
    game.doAction(new AddStaticAction(event.getRefPermanent(), MANA(event.getChosenColor())));
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
