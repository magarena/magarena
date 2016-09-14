def SPELL_THAT_TARGETS_SOURCE = new MagicStackFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack target) {
        return target.isSpell() && target.getTarget() == source;
    }
};

def TARGET_SPELL_THAT_TARGETS_SN = {
    final MagicPermanent source ->
    return new MagicTargetChoice(
        SPELL_THAT_TARGETS_SOURCE,
        MagicTargetHint.Negative,
        "target spell that targets ${source.getName()}"
    );
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{U}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_SPELL_THAT_TARGETS_SN(source),
                this,
                "Counter target spell that targets SN."
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
