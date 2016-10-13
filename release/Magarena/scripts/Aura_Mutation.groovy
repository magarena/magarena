[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target enchantment\$. " +
                "PN creates X 1/1 green Saproling creature tokens, where X is that enchantment's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 green Saproling creature token"),
                    it.getConvertedCost()
                ));
            });
        }
    }
]
