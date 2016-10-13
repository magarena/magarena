[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_SPELL,
                this,
                "Counter target spell. PN creates an X/X green Ooze creature token, where X is that spell's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack card ->
                game.doAction(new CounterItemOnStackAction(card));

                final int x = card.getConvertedCost();
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken(x, x, "green Ooze creature token")
                ));
            })
        }
    }
]
