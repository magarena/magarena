[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target creature\$ and " +
                         "2 damage to that creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,4));
                game.doAction(new MagicDealDamageAction(event.getSource(),it.getController(),2));
            });
        }
    }
]
