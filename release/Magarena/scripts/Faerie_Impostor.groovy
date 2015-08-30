[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    A_CREATURE_YOU_CONTROL
                ),
                MagicBounceTargetPicker.create(),
                this,
                "You may\$ return a creature you control to its owner's hand. " +
                "If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
            } else {
                event.processTargetPermanent(game, {
                    game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
