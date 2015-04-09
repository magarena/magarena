[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(1,true),
                this,
                "SN deals 1 damage to target creature\$. " +
                "It can't be regenerated this turn. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,1);
                damage.setNoRegeneration();
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
