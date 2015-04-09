def choice = new MagicTargetChoice("an attacking creature to sacrifice");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ sacrifices an attacking creature. " +
                "If you control three or more artifacts, he or she sacrifices two creatures instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    it,
                    choice
                ));
                if (MagicCondition.METALCRAFT_CONDITION.accept(event.getSource())) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        it,
                        choice
                    ));
                }
            });
        }
    }
]
