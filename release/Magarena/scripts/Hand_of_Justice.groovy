def UNTAPPED_WHITE_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
    {
        return target.hasType(MagicType.Creature) && target.hasColor(MagicColor.White) && 
               target.isUntapped() && 
               target.isController(player);
    }
};

def THREE_UNTAPPED_WHITE_CREATURE_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(UNTAPPED_WHITE_CREATURE_YOU_CONTROL) >= 3;
    }
};

def AN_UNTAPPED_WHITE_CREATURE_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_WHITE_CREATURE_YOU_CONTROL,"an untapped white creature you control");

[
    new MagicPermanentActivation(
        [THREE_UNTAPPED_WHITE_CREATURE_CONDITION],
        new MagicActivationHints(MagicTiming.Removal),
        "Untap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
			new MagicTapEvent(source),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_WHITE_CREATURE_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_WHITE_CREATURE_YOU_CONTROL),
                new MagicTapPermanentEvent(source, AN_UNTAPPED_WHITE_CREATURE_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return MagicRuleEventAction.create(source, "Destroy target creature.");
        }
    }
]
