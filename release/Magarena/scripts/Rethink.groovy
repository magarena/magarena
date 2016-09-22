[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {X}, where X is its converted cost. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = it.getConvertedCost();
                game.addEvent(new MagicCounterUnlessEvent(
                    event.getSource(),
                    it,
                    MagicManaCost.create(amount)
                ));
            });
        }
    }
]
