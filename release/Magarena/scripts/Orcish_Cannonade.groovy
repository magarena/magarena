[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 2 damage to target creature\$ amd 3 damage to PN." +
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicDamage damageCreature=new MagicDamage(event.getSource(),it,2);
                final MagicDamage damagePlayer=new MagicDamage(event.getSource(),event.getPlayer(),3);
                game.doAction(new MagicDealDamageAction(damageCreature));
                game.doAction(new MagicDealDamageAction(damagePlayer));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
