[
    new MagicWhenCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
            return new MagicEvent(
                card,
                new MagicMayChoice(NEG_TARGET_PLAYER),
                this,
                "PN may\$ have target player\$ lose life equal to the number of Zombies on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game, {
                    final int X = game.getNrOfPermanents(MagicSubType.Zombie);
                    game.doAction(new ChangeLifeAction(it,-X));
                });
            }
        }
    }
]
