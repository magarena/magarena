def RED_INSTANT_OR_SORCERY = new MagicStackFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
        return target.hasColor(MagicColor.Red) && target.isInstantOrSorcerySpell();
    }
};

def TARGET_RED_INSTANT_OR_SORCERY = new MagicTargetChoice(
    RED_INSTANT_OR_SORCERY,
    "target red instant or sorcery spell"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
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
                TARGET_RED_INSTANT_OR_SORCERY,
                this,
                "Counter target red instant or sorcery spell\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new CounterItemOnStackAction(it));
            });
        }
    }
]
