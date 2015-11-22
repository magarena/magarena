def SPELL_WITH_CONVERTED_COST_X_OR_LESS = {
    final int X ->
    return new MagicStackFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicItemOnStack target) {
            return target.isSpell() && target.getConvertedCost() <= X;
        } 
    };
}

def TARGET_SPELL_WITH_CONVERTED_COST_X_OR_LESS = {
    final int X ->
    return new MagicTargetChoice(
        SPELL_WITH_CONVERTED_COST_X_OR_LESS(X),
        "target spell with converted mana cost X or less"
    );
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int X = permanent.getController().getNrOfPermanents(MagicSubType.Faerie);
            return new MagicEvent(
                permanent,
                TARGET_SPELL_WITH_CONVERTED_COST_X_OR_LESS(X),
                this,
                "Counter target spell\$ with converted mana cost X or less, " + 
                "where X is the number of Faeries you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new CounterItemOnStackAction(it));
            });
        }
    }
]
