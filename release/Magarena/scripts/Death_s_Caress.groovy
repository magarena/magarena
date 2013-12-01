[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature\$. If that creature was " +
                "a Human, you gain life equal to its toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicDestroyAction(creature));
                if (creature.hasSubType(MagicSubType.Human)) {
                    game.doAction(new MagicChangeLifeAction(
                        event.getPlayer(),
                        creature.getToughness()
                    ));
                }
            });
        }
    }
]
