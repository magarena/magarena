def DEMON_CREATURE_CARD_FROM_LIBRARY=new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Creature) && target.hasSubType(MagicSubType.Demon)
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Library;
    }
};

def CREATURE_NAMED_SHADOWBORN_APOSTLE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.getName().equals("Shadowborn Apostle") && target.isController(player) && target.isCreature();
    }
};

def SIX_SHADOWBORN_APOSTLE_CONDITION =new MagicCondition() {
    public boolean accept(final MagicSource source) {
        return source.getController().getNrOfPermanents(CREATURE_NAMED_SHADOWBORN_APOSTLE_YOU_CONTROL)>=6;
    }
};

def CREATURE_NAMED_SHADOWBORN_APOSTLE = new MagicTargetChoice(
    CREATURE_NAMED_SHADOWBORN_APOSTLE_YOU_CONTROL,
    MagicTargetHint.None,
    "a creature named Shadowborn Apostle to sacrifice"
);

[
    new MagicPermanentActivation(
        [SIX_SHADOWBORN_APOSTLE_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{B}"),
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    CREATURE_NAMED_SHADOWBORN_APOSTLE
                ),
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    CREATURE_NAMED_SHADOWBORN_APOSTLE
                ),                
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    CREATURE_NAMED_SHADOWBORN_APOSTLE
                ),                
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    CREATURE_NAMED_SHADOWBORN_APOSTLE
                ),                
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    CREATURE_NAMED_SHADOWBORN_APOSTLE
                ),                
                new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    CREATURE_NAMED_SHADOWBORN_APOSTLE
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a Demon creature card and puts that card onto the battlefield. Then shuffles PN's library."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicTargetChoice(
                    DEMON_CREATURE_CARD_FROM_LIBRARY,
                    "a Demon creature card from your library"
                )
            ));
        }
    }
]
