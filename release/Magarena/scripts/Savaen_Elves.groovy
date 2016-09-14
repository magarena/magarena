def AURA_ATTACHED_TO_A_LAND = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Aura) &&
            target.getEnchantedPermanent().hasType(MagicType.Land)
    }
};

def NEG_AURA_ATTACHED_TO_A_LAND = new MagicTargetChoice(
    AURA_ATTACHED_TO_A_LAND,
    MagicTargetHint.Negative,
    "target Aura attached to a land"
);
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{G}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_AURA_ATTACHED_TO_A_LAND,
                this,
                "Destroy target Aura attached to a land.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
            });
        }
    }
]
