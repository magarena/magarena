[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_ARTIFACT_OR_CREATURE,
                MagicCopyPermanentPicker.create(),
                this,
                "Put a token onto the battlefield that's a copy of target artifact or creature\$. " +
                "Cipher."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                game.doAction(new PlayTokenAction(
                    player,
                    it
                ));
                game.doAction(new CipherAction(
                    event.getCardOnStack(),
                    player
                ));
            });
        }
    }
]
