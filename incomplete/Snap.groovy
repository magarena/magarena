[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Return target creature\$ to its owner's hand. Untap up to two lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            });
            game.addEvent(new MagicRepeatedPermanentsEvent(
                event.getSource(),
                TARGET_LAND,
                2,
                MagicChainEventFactory.Untap
            ));
        }
    }
]
