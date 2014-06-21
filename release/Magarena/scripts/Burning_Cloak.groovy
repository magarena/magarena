[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicDefaultTargetPicker.create(), //Leave options open, could be pump or damage
                this,
                "Target creature\$ gets +2/+0 until end of turn. SN deals 2 damage to that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicChangeTurnPTAction(creature,2,0));
                final MagicDamage damage = new MagicDamage(event.getSource(),creature,2)
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
