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
            ATTACKING_CREATURE.filter(event) each {
                if (it.hasColor(MagicColor.Red)) {
                    game.doAction(new ChangeTurnPTAction(it,2,0));
                }
                if (it.hasColor(MagicColor.White)) {
                    game.doAction(new ChangeTurnPTAction(it,0,2));
                }
            }
        }
    }
]
