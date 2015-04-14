[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target player\$. " +
                "Then if that player has more life than you, " +
                "return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,4));
                final boolean more = it.getLife() > event.getPlayer().getLife();
                if (more) {
                    game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
