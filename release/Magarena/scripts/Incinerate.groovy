[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(3,true),
                this,
                "SN deals 3 damage to target creature or player\$. " +
                "A creature dealt damage this way can't be regenerated this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,3);
                damage.setNoRegeneration();
                game.doAction(new DealDamageAction(damage));
            });
        }
    }
]
