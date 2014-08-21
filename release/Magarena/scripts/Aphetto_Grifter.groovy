def UNTAPPED_WIZARD_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Wizard) &&  
               target.isUntapped() && 
               target.isController(player);
    }
};

def TWO_UNTAPPED_WIZARD_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(UNTAPPED_WIZARD_YOU_CONTROL) >= 2;
    }
};

def AN_UNTAPPED_WIZARD_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_WIZARD_YOU_CONTROL,"an untapped Wizard you control");
            
def sourceEvent = MagicRuleEventAction.create("Tap target permanent.");

[
    new MagicPermanentActivation(
        [TWO_UNTAPPED_WIZARD_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Tap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapPermanentEvent(source, AN_UNTAPPED_WIZARD_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_WIZARD_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return sourceEvent.getEvent(source);
        }
    }
]
