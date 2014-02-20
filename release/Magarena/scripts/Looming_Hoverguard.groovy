[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_ARTIFACT,
                MagicBounceTargetPicker.create(),
                this,
                "Put target artifact\$ on top of its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent artifact ->
                game.doAction(new MagicRemoveFromPlayAction(artifact,MagicLocationType.TopOfOwnersLibrary));
            });
        }
    }
]
