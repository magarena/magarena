[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicBounceTargetPicker.create(),
                this,
                "Return target creature\$ to its owner's hand. PN gains 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),2));
            });
        }
    }
]
