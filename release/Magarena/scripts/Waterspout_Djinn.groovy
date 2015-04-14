def UNTAPPED_ISLAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isUntapped() && target.hasSubType(MagicSubType.Island) && target.isController(player);
    } 
};

def AN_UNTAPPED_ISLAND_YOU_CONTROL = new MagicTargetChoice(
    UNTAPPED_ISLAND_YOU_CONTROL,
    "an untapped Island you control"
);

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Return an untapped Island you control to its owner's hand?"),
                this,
                "PN may\$ return an untapped Island PN controls to its owner's hand. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent cost = new MagicBounceChosenPermanentEvent(event.getSource(), event.getPlayer(), AN_UNTAPPED_ISLAND_YOU_CONTROL);      
            if (event.isYes() && cost.isSatisfied()) {
                game.addEvent(cost);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
