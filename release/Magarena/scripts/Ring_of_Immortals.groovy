def INSTANT_OR_AURA_TARGETS_YOUR_PERM = new MagicStackFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
        return (target.hasType(MagicType.Instant) || target.hasSubType(MagicSubType.Aura)) &&
            target.isSpell() &&
            target.getTarget().isPermanent() &&
            target.getTarget().isFriend(player);
    }
};

def TARGET_INSTANT_OR_AURA_TARGETS_YOUR_PERM = new MagicTargetChoice(
    INSTANT_OR_AURA_TARGETS_YOUR_PERM,
    MagicTargetHint.Negative,
    "target instant or Aura spell that targets a permanent you control"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_INSTANT_OR_AURA_TARGETS_YOUR_PERM,
                this,
                "Counter target instant or Aura spell that targets a permanent PN controls.\$"
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
