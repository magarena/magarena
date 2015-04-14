[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target permanent\$ to its owner's hand. If that permanent is red or green, "+
                "put it on top of its owner's library instead."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (it.hasColor(MagicColor.Red) || it.hasColor(MagicColor.Green)) {
                    game.doAction(new RemoveFromPlayAction(
                        it,
                        MagicLocationType.TopOfOwnersLibrary
                    ));
                } else {
                    game.doAction(new RemoveFromPlayAction(
                        it,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    }
]
