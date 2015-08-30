[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONLAND_PERMANENT,
                MagicBounceTargetPicker.create(),
                this,
                "Return target nonland permanent\$ and all other " +
                "permanents with the same name as that permanent " +
                "to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveAllFromPlayAction(
                    new MagicNameTargetFilter(
                        PERMANENT,
                        it.getName()
                    ).filter(event),
                    MagicLocationType.OwnersHand
                ));
            });
        }
    }
]
