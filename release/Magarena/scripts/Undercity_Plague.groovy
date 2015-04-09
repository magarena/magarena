[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ loses 1 life, discards a card, then sacrifices a permanent. Cipher."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicChangeLifeAction(it,-1));
                game.addEvent(new MagicDiscardEvent(event.getSource(),it));
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    it,
                    SACRIFICE_PERMANENT
                ));
                game.doAction(new MagicCipherAction(event.getCardOnStack(),event.getPlayer()));
            });
        }
    }
]
