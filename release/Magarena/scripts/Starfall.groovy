[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target creature.\$ If that creature is an enchantment, "+
                "SN deals 3 damage to that creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->
                final MagicPlayer controller = target.getController();
                game.doAction(new DealDamageAction(event.getSource(), target, 3));
                if (target.hasType(MagicType.Enchantment)) {
                    game.doAction(new DealDamageAction(event.getSource(), controller, 3));
                    game.logAppendMessage(event.getPlayer(), event.getSource().getName()+" deals 3 damage to "+controller.getName()+".");
                }
            });
        }
    }
]
