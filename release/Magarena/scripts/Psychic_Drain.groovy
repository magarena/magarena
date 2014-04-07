[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,  
                amount,
                this,
                "Target player puts the top RN cards of his or her library into his or her graveyard and PN gains RN life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                game.doAction(new MagicMillLibraryAction(player,event.getRefInt()));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(), event.getRefInt()));
            });
        }
    }
]
