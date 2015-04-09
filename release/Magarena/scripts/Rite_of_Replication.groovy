[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicCopyPermanentPicker.create(),
                this,
                "Put a token onto the battlefield that's a copy of target creature\$. " +
                "If SN was kicked, put five of those tokens onto the battlefield instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                int count = event.isKicked() ? 5 : 1;
                game.doAction(new MagicPlayTokensAction(
                    player,
                    it,
                    count
                ));
            });
        }
    }
]
