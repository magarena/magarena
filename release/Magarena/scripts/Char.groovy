[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target creature or player\$ and 2 damage to you."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage1=new MagicDamage(event.getSource(),target,4);
                    game.doAction(new MagicDealDamageAction(damage1));
                    final MagicDamage damage2=new MagicDamage(event.getSource(),event.getPlayer(),2);
                    game.doAction(new MagicDealDamageAction(damage2));
                }
            });
        }
    }
]
