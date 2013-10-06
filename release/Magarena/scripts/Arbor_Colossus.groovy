[
    new MagicWhenBecomesMonstrousTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicChangeStateAction action) {
            return action.permanent == permanent ? 
                new MagicEvent(
                    permanent,
                    new MagicTargetChoice("target creature with flying an opponent controls"),
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target creature\$ with flying an opponent controls."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
            });
        }
    }
]
