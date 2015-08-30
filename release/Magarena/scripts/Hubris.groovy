[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicBounceTargetPicker.create(),
                this,
                "Return target creature\$ and all Auras attached to it to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanentList all = new MagicPermanentList();
                all.add(it);
                all.addAll(it.getAuraPermanents());
                game.doAction(new RemoveAllFromPlayAction(all, MagicLocationType.OwnersHand));
            });
        }
    }
]
