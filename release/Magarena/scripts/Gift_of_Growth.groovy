[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Untap target creature\$. It gets +2/+2 until end of turn. " +
                "If SN was kicked, that creature gets +4/+4 instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new UntapAction(it));
                int amount = event.isKicked() ? 4 : 2;
                game.doAction(new ChangeTurnPTAction(it, amount, amount));
            });
        }
    }
]
