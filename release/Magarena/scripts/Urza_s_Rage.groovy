[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.isKicked() ? 10 : 3;
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals 3 damage to target creature or player\$. "+
                "If SN was kicked, instead it deals 10 damage to that creature or player and the damage can't be prevented."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.isKicked() ? 10 : 3;
                final MagicDamage damage=new MagicDamage(event.getSource(),it,amount);
                if (event.isKicked()) {
                    damage.setUnpreventable();
                }
                game.doAction(new DealDamageAction(damage));
            });
        }
    }
]
