[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "SN deals damage equal to the " +
                "number of cards in target player's hand to that player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    it,
                    it.getHandSize()
                );
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
