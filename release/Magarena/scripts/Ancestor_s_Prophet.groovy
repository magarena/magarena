def UNTAPPED_CLERIC_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
    {
        return target.hasSubType(MagicSubType.Cleric) && 
               target.isUntapped() && 
               target.isController(player);
    }
};

def FIVE_UNTAPPED_CLERIC_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(UNTAPPED_CLERIC_YOU_CONTROL) >= 5;
    }
};

def AN_UNTAPPED_CLERIC_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_CLERIC_YOU_CONTROL,"an untapped Cleric you control");
               
def sourceEvent = MagicRuleEventAction.create("Gain 10 life.");

[
    new MagicPermanentActivation(
        [FIVE_UNTAPPED_CLERIC_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "+Life"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CLERIC_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CLERIC_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CLERIC_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CLERIC_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CLERIC_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return sourceEvent.getEvent(source);
        }
    }
]
