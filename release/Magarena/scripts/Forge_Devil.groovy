[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_CREATURE,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature\$ and 1 damage to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage1 = new MagicDamage(permanent,target,1);
                    game.doAction(new MagicDealDamageAction(damage1));
                    final MagicDamage damage2 = new MagicDamage(permanent,event.getPlayer(),1);
                    game.doAction(new MagicDealDamageAction(damage2));
                }
            });
        }
    }
]
