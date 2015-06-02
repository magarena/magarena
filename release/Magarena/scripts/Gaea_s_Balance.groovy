def FOREST_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Forest);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def ISLAND_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Island);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def MOUNTAIN_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Mountain);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def PLAINS_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Plains);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def SWAMP_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Swamp);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,

                this,
                "Search your library for a land card of each basic land type and put them onto the battlefield."+
                " Then shuffle your library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    FOREST_CARD_FROM_LIBRARY,
                    1, 
                    true, 
                    "to put onto the battlefield"
                )
            ));
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    ISLAND_CARD_FROM_LIBRARY,
                    1, 
                    true, 
                    "to put onto the battlefield"
                )
            ));
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    MOUNTAIN_CARD_FROM_LIBRARY,
                    1, 
                    true, 
                    "to put onto the battlefield"
                )
            ));
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    PLAINS_CARD_FROM_LIBRARY,
                    1, 
                    true, 
                    "to put onto the battlefield"
                )
            ));
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    SWAMP_CARD_FROM_LIBRARY,
                    1, 
                    true, 
                    "to put onto the battlefield"
                )
            ));
        }
    }
]
