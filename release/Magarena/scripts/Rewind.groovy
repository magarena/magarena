[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$. Untap up to four lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCounterItemOnStackAction(it));
            });
            game.addEvent(new MagicRepeatedPermanentsEvent(
                event.getSource(),
                TARGET_LAND,
                4,
                MagicChainEventFactory.Untap
            ));
        }
    }
]
