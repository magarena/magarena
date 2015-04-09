[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                this,
                "Attacking red creatures get +2/+0 and attacking white creatures get +0/+2 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.filterPermanents(ATTACKING_CREATURE) each {
                if (it.hasColor(MagicColor.Red)) {
                    game.doAction(new MagicChangeTurnPTAction(it,2,0));
                }
                if (it.hasColor(MagicColor.White)) {
                    game.doAction(new MagicChangeTurnPTAction(it,0,2));
                }
            }
        }
    }
]
