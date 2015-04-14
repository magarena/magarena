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
                if (it.isEnchanted()) {
                    final MagicPermanentList auras = new MagicPermanentList();
                    auras.addAll(it.getAuraPermanents())
                    for (final MagicPermanent aura : auras) {
                        game.doAction(new RemoveFromPlayAction(aura,MagicLocationType.OwnersHand));
                    }
                }
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            })
        }
    }
]
