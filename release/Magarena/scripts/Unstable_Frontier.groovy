def SUBTYPE = {
    final MagicColor color ->
    return new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.removeAll();
            flags.add(color.getLandSubType());
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
    game.doAction(new AddStaticAction(event.getRefPermanent(), SUBTYPE(event.getChosenColor())));
    game.doAction(new AddStaticAction(event.getRefPermanent(), MANA(event.getChosenColor())));
};

def TARGET_LAND_YOU_CONTROL = new MagicTargetChoice("target land you control");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Becomes"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_LAND_YOU_CONTROL,
                MagicDefaultTargetPicker.create(),
                this,
                "Target land PN controls\$ becomes the basic land type of his or her choice until end of turn."
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
