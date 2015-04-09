[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature\$. If that creature was " +
                "a Human, you gain life equal to its toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                if (it.hasSubType(MagicSubType.Human)) {
                    game.doAction(new MagicChangeLifeAction(
                        event.getPlayer(),
                        it.getToughness()
                    ));
                }
            });
        }
    }
]
