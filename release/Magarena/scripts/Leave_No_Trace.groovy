[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target enchantment\$ and each other enchantment that shares a color with it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->

                final Set<MagicColor> targetColors = new HashSet<MagicColor>();
                for (final MagicColor color : MagicColor.values()) {
                    if(target.hasColor(color)) {
                        targetColors.add(color);
                    }
                }

                final Set<MagicPermanent> targetList = new HashSet<MagicPermanent>();
                for (final MagicPermanent enchantment : ENCHANTMENT.filter(event)) {
                    for (final MagicColor color : targetColors) {
                        if (enchantment.hasColor(color)) {
                            targetList.add(enchantment);
                            break;
                        }
                    }
                }

                for (final MagicPermanent enchantment : targetList) {
                    game.doAction(new DestroyAction(enchantment));
                }
            });
        }
    }
]
