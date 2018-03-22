[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals damage to target creature or player\$ equal to the number of cards in PN's hand. " +
                "PN discards all the cards in PN's hand, then draw that many cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getHandSize();
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
                game.addEvent(new MagicDiscardHandEvent(event.getSource()));
                game.addEvent(new MagicDrawEvent(event.getSource(), event.getPlayer(), amount));
            });
        }
    }
]

