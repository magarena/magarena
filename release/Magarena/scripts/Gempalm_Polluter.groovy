[
    new MagicWhenCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
            return new MagicEvent(
                card,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "When you cycle SN, you may have target player\$ lose X life, "+
                "where X is the number of Zombies on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,{
                final int X = game.getNrOfPermanents(MagicSubType.Zombie);
                game.doAction(new MagicChangeLifeAction(it,-X));
            });
        }
    }
]
