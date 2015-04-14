def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[    
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().controlsPermanent(MagicSubType.Swamp) == false;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new PutStateTriggerOnStackAction(
                EFFECT.getEvent(source)
            ));
        }
    }
]
