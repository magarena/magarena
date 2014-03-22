[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target permanent\$ to its owner's hand. If that permanent is red or green, "+
                "put it on top of its owner's library instead."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                if (permanent.hasColor(MagicColor.Red) || permanent.hasColor(MagicColor.Green)) {
                    game.doAction(new MagicRemoveFromPlayAction(
                        permanent,
                        MagicLocationType.TopOfOwnersLibrary
                    ));
                } else {
                    game.doAction(new MagicRemoveFromPlayAction(
                        permanent,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    }
]
