[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target permanent\$ to its owner's hand. Then that player discards a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.OwnersHand));
                game.addEvent(new MagicDiscardEvent(event.getSource(),permanent.getOwner()));
            });
        }
    }
]
