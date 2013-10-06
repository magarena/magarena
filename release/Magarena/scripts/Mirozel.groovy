[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return target.containsInChoiceResults(permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Return SN to its owner's hand."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRemoveFromPlayAction(
                event.getPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    }
]
