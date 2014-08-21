def UNTAPPED_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
    {
        return target.hasType(MagicType.Creature) && 
               target.isUntapped() && 
               target.isController(player);
    }
};

def TWO_UNTAPPED_CREATURE_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(UNTAPPED_CREATURE_YOU_CONTROL) >= 2;
    }
};

def AN_UNTAPPED_CREATURE_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_CREATURE_YOU_CONTROL,"an untapped creature you control");
            
def EFFECT = MagicRuleEventAction.create("Return target permanent to its owner's hand.");

[
    new MagicPermanentActivation(
        [TWO_UNTAPPED_CREATURE_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Bounce"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CREATURE_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CREATURE_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return EFFECT.getEvent(source);
        }
    }
]
