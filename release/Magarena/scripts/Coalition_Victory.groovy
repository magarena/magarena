[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "You win the game if you control a land of each basic land type and a creature of each color."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.controlsPermanent(MagicSubType.Forest) &&
                player.controlsPermanent(MagicSubType.Island) &&
                player.controlsPermanent(MagicSubType.Mountain) &&
                player.controlsPermanent(MagicSubType.Plains) &&
                player.controlsPermanent(MagicSubType.Swamp) &&
                player.controlsPermanent(BLACK_CREATURE) &&
                player.controlsPermanent(BLUE_CREATURE) &&
                player.controlsPermanent(GREEN_CREATURE) &&
                player.controlsPermanent(RED_CREATURE) &&
                player.controlsPermanent(WHITE_CREATURE)) {
                    game.doAction(new LoseGameAction(player.getOpponent()));
            }
        }
    }
]
