[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                this,
                "Copy target instant or sorcery spell\$. " +
                "PN may choose new targets for the copy. " +
                "If the buyback cost was payed, return SN to its owner's hand as it resolves."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),it));
                if (event.isBuyback()) {
                    game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
