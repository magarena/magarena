def UNTAPPED_MERFOLK_YOU_CONTROL=new MagicPermanentFilterImpl(){
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
        {
            return target.hasSubType(MagicSubType.Merfolk) && !target.isTapped() && !target.isOpponent(player);
        }
    };
def TWO_UNTAPPED_MERFOLK_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicTargetFilter<MagicPermanent> filter =new MagicOtherPermanentTargetFilter(
            UNTAPPED_MERFOLK_YOU_CONTROL, 
            (MagicPermanent)source
        );
        final MagicGame game = source.getGame();
        final MagicPlayer player = source.getController();
        return game.filterPermanents(player, filter).size() >= 2;
    }
};
def AN_UNTAPPED_MERFOLK_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_MERFOLK_YOU_CONTROL,"an untapped Merfolk you control");

[
    new MagicWhenComesIntoPlayTrigger() 
    {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) 
        {
            return new MagicEvent(permanent,this,"PN puts two 1/1 blue Merfolk Wizard creature token onto the battlefield.");
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) 
        {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("1/1 blue Merfolk Wizard creature token")));
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("1/1 blue Merfolk Wizard creature token")));
        }
    },
    new MagicPermanentActivation([TWO_UNTAPPED_MERFOLK_CONDITION],new MagicActivationHints(MagicTiming.Removal),"Untap") 
    {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) 
        {
            return [new MagicTapPermanentEvent(source, AN_UNTAPPED_MERFOLK_YOU_CONTROL),new MagicTapPermanentEvent(source, AN_UNTAPPED_MERFOLK_YOU_CONTROL)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost)
        {
            
            return new MagicEvent(source,source,this,"Untap SN. SN gains shroud until end of turn.");
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event)
        {
            game.doAction(new MagicUntapAction(event.getPermanent()));
            game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Shroud));
        }
      
    }
]
