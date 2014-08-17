[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature or player\$. " +
                "Scry 2."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                final MagicDamage damage=new MagicDamage(event.getSource(),target,2);
                game.doAction(new MagicDealDamageAction(damage));
                game.addEvent(new MagicScryXEvent(event.getSource(),event.getPlayer(),2));
            });
        }
    }
]
