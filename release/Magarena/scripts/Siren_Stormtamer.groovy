def SPELL_OR_ABILITY_THAT_TARGETS_YOU_OR_A_CREATURE_YOU_CONTROL = new MagicStackFilterImpl() {
    @Override
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
        final MagicTarget target = item.getTarget();
        return player.getId() == target.getId() ||
            (target.hasType(MagicType.Creature) && player.isFriend(target));
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{U}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice(
                    SPELL_OR_ABILITY_THAT_TARGETS_YOU_OR_A_CREATURE_YOU_CONTROL,
                    "target spell or ability that targets you or a creature you control"
                ),
                this,
                "Counter target spell or ability that targets PN or a creature PN controls\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetItemOnStack(game, {
                game.doAction(new CounterItemOnStackAction(it));
            });
        }
    }
]


