[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ENCHANTMENT,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target enchantment\$. " +
                "Put X 1/1 green Saproling creature tokens onto the battlefield, where X is that enchantment's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.doAction(new MagicPlayTokensAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 green Saproling creature token"),
                    it.getConvertedCost()
                ));
            });
        }
    }
]
