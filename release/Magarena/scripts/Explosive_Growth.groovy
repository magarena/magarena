[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Target creature\$ gets +2/+2 until end of turn. " +
                "If SN was kicked, that creature gets +5/+5 instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                int amount = event.isKicked() ? 5 : 2;
                game.doAction(new ChangeTurnPTAction(it, amount, amount));
            });
        }
    }
]
