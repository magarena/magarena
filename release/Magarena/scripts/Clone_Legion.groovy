[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER_CONTROLS_CREATURE,
                this,
                "For each creature target player\$ controls, PN creates a token that's a copy of that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                CREATURE_YOU_CONTROL.filter(it) each {
                    game.doAction(new PlayTokenAction(event.getPlayer(), it));
                }
            });
        }
    }
]
