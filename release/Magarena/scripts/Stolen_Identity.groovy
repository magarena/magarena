[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_ARTIFACT_OR_CREATURE,
                MagicCopyTargetPicker.create(),
                this,
                "Put a token onto the battlefield that's a copy of target artifact or creature\$. " +
                "Cipher."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPlayer player = event.getPlayer();
                    game.doAction(new MagicPlayCardAction(
                        MagicCard.createTokenCard(creature.getCardDefinition(), player),
                        player
                    ));
                    game.doAction(new MagicCipherAction(
                        event.getCardOnStack(), 
                        event.getPlayer()
                    ));
                }
            });
        }
    }
]
