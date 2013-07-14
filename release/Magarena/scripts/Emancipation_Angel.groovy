[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.PERMANENT_YOU_CONTROL,
                MagicBounceTargetPicker.create(),
                this,
                "Return a permanent\$ you control to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicRemoveFromPlayAction(
                        permanent,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    }
]
