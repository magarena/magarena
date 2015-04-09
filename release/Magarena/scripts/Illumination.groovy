def choice = Negative("target artifact or enchantment spell");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                this,
                "Counter target artifact or enchantment spell.\$  Its controller gains life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCounterItemOnStackAction(it));
                game.doAction(new MagicChangeLifeAction(it.getController(),it.getConvertedCost()));
                game.logAppendMessage(event.getPlayer(),"("+it.getConvertedCost()+")");
            });
        }
    }
]
