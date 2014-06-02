[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.Negative("target artifact or enchantment spell"),
                this,
                "Counter target artifact or enchantment spell.\$  Its controller gains life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack targetSpell ->
                game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                game.doAction(new MagicChangeLifeAction(targetSpell.getController(),targetSpell.getConvertedCost()));
                game.logAppendMessage(event.getPlayer(),"("+targetSpell.getConvertedCost()+")");
            });
        }
    }
]
