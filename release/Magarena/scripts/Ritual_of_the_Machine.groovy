def NONARTIFACT_NONBLACK_CREATURE=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasColor(MagicColor.Black) && !target.hasType(MagicType.Artifact) && target.isCreature();
    } 
};

def TARGET_NONARTIFACT_NONBLACK_CREATURE = new MagicTargetChoice(
    NONARTIFACT_NONBLACK_CREATURE,
    "target nonartifact, nonblack creature"
);
[
    new MagicSpellCardEvent() {  
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {      
        return new MagicEvent(
                cardOnStack,
                TARGET_NONARTIFACT_NONBLACK_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target nonartifact, nonblack creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainControlAction(
                    event.getPlayer(),
                    it
                ));
            });
        }
    }
]
