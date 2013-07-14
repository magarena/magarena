[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.ARTIFACT_YOU_CONTROL,
                    MagicBounceTargetPicker.create(),
                    this,
                    "Return an artifact you control to its owner's hand."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent artifact) {
                    game.doAction(new MagicRemoveFromPlayAction(artifact,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
