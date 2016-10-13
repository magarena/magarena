[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicCopyPermanentPicker.create(),
                this,
                "PN creates a token that's a copy of target creature\$. " +
                "If SN was kicked, create five of those tokens instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                int count = event.isKicked() ? 5 : 1;
                game.doAction(new PlayTokensAction(
                    player,
                    it,
                    count
                ));
            });
        }
    }
]
