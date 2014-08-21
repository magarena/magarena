def UNTAPPED_GOBLIN_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
    {
        return target.hasSubType(MagicSubType.Goblin) && 
               target.isUntapped() && 
               target.isController(player);
    }
};

def FIVE_UNTAPPED_GOBLIN_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(UNTAPPED_GOBLIN_YOU_CONTROL) >= 5;
    }
};

def AN_UNTAPPED_GOBLIN_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_GOBLIN_YOU_CONTROL,"an untapped Goblin you control");
                
def EFFECT = MagicRuleEventAction.create("SN deals 10 damage to each creature and each player.");

[
    new MagicPermanentActivation(
        [FIVE_UNTAPPED_GOBLIN_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_GOBLIN_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return EFFECT.getEvent(source);
        }
    }
]
