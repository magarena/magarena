[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target enchantment\$. PN gains life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(), it.getConvertedCost()));
            });
        }
    }
]
