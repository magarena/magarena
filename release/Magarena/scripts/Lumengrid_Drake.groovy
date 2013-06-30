[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return MagicCondition.METALCRAFT_CONDITION.accept(permanent) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE,
                    MagicBounceTargetPicker.create(),
                    this,
                    "Return target creature\$ to its owner's hand."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
