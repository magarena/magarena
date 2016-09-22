[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {1} for each basic land type among lands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = event.getPlayer().getDomain();
                game.logAppendValue(event.getPlayer(),amount);
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create(amount)));
            });
        }
    }
]
