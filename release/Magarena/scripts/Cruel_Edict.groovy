[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Target opponent\$ sacrifices a creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer opponent) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        opponent,
                        MagicTargetChoice.SACRIFICE_CREATURE
                    ));
                }
            });
        }
    }
]
