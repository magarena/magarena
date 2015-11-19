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
                "PN may\$ return a creature he or she controls to its owner's hand. " +
                "If PN doesn't, sacrifice SN."
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
