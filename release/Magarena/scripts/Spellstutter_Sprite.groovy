def SPELL_WITH_CONVERTED_COST_X_OR_LESS=new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        return target.isSpell() && target.getConvertedCost() <= player.getNrOfPermanents(MagicSubType.Faerie);
    } 
};

def TARGET_SPELL_WITH_CONVERTED_COST_X_OR_LESS = new MagicTargetChoice(
    SPELL_WITH_CONVERTED_COST_X_OR_LESS,
    "target spell with converted mana cost X or less"
);

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_SPELL_WITH_CONVERTED_COST_X_OR_LESS,
                this,
                "Counter target spell\$ with converted mana cost X or less," + "where X is the number of Faeries you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                    game.doAction(new MagicCounterItemOnStackAction(it));
                });
        }
    }
]
