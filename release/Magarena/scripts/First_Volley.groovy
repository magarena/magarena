[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature\$ and 1 damage to that creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new DealDamageAction(event.getSource(), permanent, 1));
                game.doAction(new DealDamageAction(event.getSource(), permanent.getController(), 1));
            })
        }
    }
]

