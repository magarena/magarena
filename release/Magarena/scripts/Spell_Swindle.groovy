[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$. " +
                "Create X colorless Treasure artifact tokens, where X is that spell's converted mana cost. " +
                "They have \"{T}, Sacrifice this artifact: Add one mana of any color to your mana pool.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetItemOnStack(game, {
                game.doAction(new CounterItemOnStackAction(it));
                it.getConvertedCost().times {
                    game.doAction(new PlayTokenAction(
                        event.getPlayer(),
                        CardDefinitions.getToken("colorless Treasure artifact token")
                    ));
                }
            });
        }
    }
]


