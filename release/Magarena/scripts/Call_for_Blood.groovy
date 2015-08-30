[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                payedCost.getTarget(),
                this,
                "Target creature gets -X/-X until end of turn, where X is RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefPermanent().getPower();
            game.logAppendX(event.getPlayer(),amount);
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, -amount, -amount));
            });
        }
    }
]
