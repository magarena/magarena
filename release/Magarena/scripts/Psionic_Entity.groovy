[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature\$ or player\$ and 3 damage to itself."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,2);
                game.doAction(new MagicDealDamageAction(damage));
                final MagicDamage damage2=new MagicDamage(event.getSource(),event.getPermanent(),3);
                game.doAction(new MagicDealDamageAction(damage2));
            });
        }
    }
]
