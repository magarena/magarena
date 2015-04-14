[
    new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                permanent.getController(),
                PERMANENT_YOU_CONTROL.except(permanent)
            );
            for (final MagicPermanent perm : targets) {
                if (itemOnStack.containsInChoiceResults(perm)) {
                    return new MagicEvent(
                        permanent,
                        new MagicMayChoice(),
                        perm,
                        this,
                        "PN may\$ return RN to its owner's hand."
                    );
                }
            }
            return MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new RemoveFromPlayAction(event.getRefPermanent(),MagicLocationType.OwnersHand));
            }
        }
    }
]
