[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            return act.isPermanent(permanent) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,
                    MagicBounceTargetPicker.create(),
                    this,
                    "Put target nonland permanent\$ on top of its owner's library."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
           event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.TopOfOwnersLibrary));
            });
        }
    }
]
