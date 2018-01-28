def NEG_TARGET_FOREST_GREEN_ENCHANTMENT_OR_GREEN_PLANESWALKER = new MagicTargetChoice(
    new MagicPermanentFilterImpl() {
        @Override
        boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasSubType(MagicSubType.Forest)
                || (target.hasColor(MagicColor.Green)
                    && (target.hasType(MagicType.Enchantment) || target.hasType(MagicType.Planeswalker)));
        }
    },
    MagicTargetHint.Negative,
    "target Forest, green enchantment, or green planeswalker"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_FOREST_GREEN_ENCHANTMENT_OR_GREEN_PLANESWALKER,
                this,
                "Destroy target forest, green enchantment, or green planeswalker\$. " +
                "If that permanent was a Nissa planeswalker, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                if (it.hasType(MagicType.Planeswalker) && it.hasSubType(MagicSubType.Nissa)) {
                    game.doAction(new DrawAction(event.getPlayer()));
                }
            });
        }
    }
]

