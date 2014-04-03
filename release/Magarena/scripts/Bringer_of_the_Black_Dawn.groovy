def CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return true;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library;
    }
};

def A_CARD_FROM_LIBRARY = new MagicTargetChoice(
    CARD_FROM_LIBRARY,
    "a card"
);
[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ pay 2 Life to search his or her library for a card, then shuffle his or her library and put that card on top of it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                game.addEvent(new MagicPayLifeEvent(event.getPermanent(), 2));
                game.addEvent(new MagicSearchOntoLibraryEvent(event,A_CARD_FROM_LIBRARY));
            }
        }
    }
]
