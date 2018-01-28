def NEG_TARGET_BLACK_CREATURE_OR_BLACK_PLANESWALKER = new MagicTargetChoice(
    new MagicPermanentFilterImpl() {
        @Override
        boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.hasColor(MagicColor.Black) && (target.hasType(MagicType.Creature) || target.hasType(MagicType.Planeswalker))
        }
    },
    MagicTargetHint.Negative,
    "target black creature or black planeswalker"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_BLACK_CREATURE_OR_BLACK_PLANESWALKER,
                this,
                "Destroy target black creature or black planeswalker\$. " +
                "If that permanent was a Liliana planeswalker, her controller loses 3 life.
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                if (it.hasType(MagicType.Planeswalker) && it.hasSubType(MagicSubType.Liliana)) {
                    game.doAction(new ChangeLifeAction(it.getController(), -3));
                }
            });
        }
    }
]

