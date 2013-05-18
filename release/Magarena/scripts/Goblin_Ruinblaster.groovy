[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            return permanent.isKicked() ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_NONBASIC_LAND,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target nonbasic land\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent land) {
                    game.doAction(new MagicDestroyAction(land));
                }
            });
        }
    }
]
