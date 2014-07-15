[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player loses 1 life, discards a card, " +
                "sacrifices a creature, then sacrifices a land."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayersAPNAP()) {
                game.doAction(new MagicChangeLifeAction(player,-1));
            }
            for (final MagicPlayer player : game.getPlayersAPNAP()) {
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    player
                ));
            }
            for (final MagicPlayer player : game.getPlayersAPNAP()) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.SACRIFICE_CREATURE
                ));
            }
            for (final MagicPlayer player : game.getPlayersAPNAP()) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.SACRIFICE_LAND
                ));
            }
        }
    }
]
