[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTarget(),
                this,
                "RN exiles the top ten cards of his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (int i=10;i>0;i--) {
                final MagicCard card = event.getRefPlayer().getLibrary().getCardAtTop();
                if (card != MagicCard.NONE) {
                    game.doAction(new MagicRemoveCardAction(card, MagicLocationType.OwnersLibrary));
                    game.doAction(new MagicMoveCardAction(card, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
                }
            }
        }
    }
]
