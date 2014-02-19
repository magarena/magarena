def CARD_NAMED_LLANOWAR_SENTINEL = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.getName().equals("Llanowar Sentinel");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Library;
    }
};

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{1}{G}"))
                ),
                this,
                "When SN enters the battlefield, you may\$ pay {1}{G}\$. If you do, search your library for a card named Llanowar Sentinel" + 
                "and put that card onto the battlefield. Then shuffle your library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if event.isYes {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicTargetChoice(
                        CARD_NAMED_LLANOWAR_SENTINEL, 
                        "a card named Llanowar Sentinel"
                    )
                ));
            }
        }
    }
]
