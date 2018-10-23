[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(4,true),
                this,
                "SN deals 4 damage to target creature\$. " +
                "The damage can't be prevented."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicDamage damage = new MagicDamage(event.getSource(),it,4);
                damage.setUnpreventable();
                game.doAction(new DealDamageAction(damage));
            });
        }
    }
]
