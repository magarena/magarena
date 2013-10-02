[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage " +
                "to target creature\$ with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage = new MagicDamage(event.getPermanent(),creature,3);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }); 
        }
    }
]
