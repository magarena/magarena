def AURA_ATTACHED_TO_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        final MagicPermanent enchanted = target.getEnchantedPermanent();
        return target.hasSubType(MagicSubType.Aura) &&
            enchanted.isCreature() &&
            enchanted.isController(player);
    }
};

def NEG_TARGET_AURA_ATTACHED_YOUR_CREATURE = new MagicTargetChoice(
        AURA_ATTACHED_TO_CREATURE_YOU_CONTROL,
        MagicTargetHint.Negative,
        "an Aura attached to a creature you control"
    );
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_AURA_ATTACHED_YOUR_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target Aura attached to a creature PN controls.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
            })
        }
    }
]
