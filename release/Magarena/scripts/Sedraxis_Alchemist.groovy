[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return permanent.getController().controlsPermanent(MagicColor.Blue) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_NONLAND_PERMANENT,
                    this,
                    "If PN controls a blue permanent, return target nonland permanent\$ to its owner's hand."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (event.getPlayer().controlsPermanent(MagicColor.Blue)) {
                    game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
