[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature\$ and each other creature that shares a color with it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicPermanent creature : CREATURE.filter(event)) {
                    if (creature == it || creature.shareColor(it)) {
                        game.doAction(new DealDamageAction(event.getSource(), creature, 2));
                    }
                }
            });
        }
    }
]
