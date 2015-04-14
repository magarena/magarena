[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    Other("target creature you control", permanent)
                ),
                MagicBounceTargetPicker.create(),
                this,
                "PN may\$ return another target creature\$ PN controls to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
