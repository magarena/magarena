[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_PLAYER,
                this,
                "Target player\$ takes an extra turn after this one. " +
                "If the buyback cost was payed, return SN to its owner's hand as it resolves."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicChangeExtraTurnsAction(it,1));
                if (event.isBuyback()) {
                    game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
