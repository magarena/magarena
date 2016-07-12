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
                final MagicPermanentList targetList = new MagicPermanentList();
                for (final MagicPermanent enchantment : ENCHANTMENT.filter(event)) {
                    if (enchantment == it || enchantment.shareColor(it)) {
                        targetList.add(enchantment);
                    }
                }
                game.doAction(new DestroyAction(targetList));
            });
        }
    }
]
