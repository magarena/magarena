[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(10),
                this,
                "SN deals 10 damage to target player\$ and PN gains 10 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,10);
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),10));
            });
        }
    }
]
