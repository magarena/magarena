[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(4,true),
                this,
                "SN deals 3 damage to target creature\$. " +
                "A creature dealt damage this way can't be regenerated this turn. "+
                "Scry 1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,4);
                damage.setNoRegeneration();
                game.doAction(new MagicDealDamageAction(damage));
                game.addEvent(new MagicScryEvent(event));
            });
        }
    }
]
