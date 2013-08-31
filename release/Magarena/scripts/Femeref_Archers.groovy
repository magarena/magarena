[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE_WITH_FLYING,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target attacking creature\$ with flying."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),creature,4);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
