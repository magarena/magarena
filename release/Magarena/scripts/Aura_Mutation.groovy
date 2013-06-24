[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ENCHANTMENT,
                new MagicDestroyTargetPicker(true),
                this,
                "Destroy target enchantment\$. " +
                "Put X 1/1 green Saproling creature tokens onto the battlefield, where X is that enchantment's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                    game.doAction(new MagicPlayTokensAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Saproling"),
                        permanent.getConvertedCost()
                    ));
                }
            });
        }
    }
]
