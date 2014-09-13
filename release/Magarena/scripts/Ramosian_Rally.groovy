def PLAINS_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
    {
        return target.hasSubType(MagicSubType.Plains) && 
               target.isController(player);
    }
};

def UNTAPPED_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
    {
        return target.hasType(MagicType.Creature) && 
               target.isUntapped() && 
               target.isController(player);
    }
};


def PLAINS_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(PLAINS_YOU_CONTROL) >= 1 && 
        source.getController().getNrOfPermanents(UNTAPPED_CREATURE_YOU_CONTROL) >= 1;
    }
};

def AN_UNTAPPED_CREATURE_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_CREATURE_YOU_CONTROL,"an untapped creature you control");

[
     new MagicCardActivation(
        [MagicCondition.PLAINS_CONDITION, MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Tap"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicTapPermanentEvent(source, AN_UNTAPPED_CREATURE_YOU_CONTROL)
            ];
        }
    }
]
