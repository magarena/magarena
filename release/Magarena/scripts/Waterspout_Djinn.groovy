def UNTAPPED_ISLAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
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
                new MagicMayChoice("Return an untapped Island to your hand?"),
                this,
                "PN may\$ return an untapped Island to his or her hand. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().controlsPermanent(UNTAPPED_ISLAND_YOU_CONTROL) == true) {
                game.addEvent(new MagicBounceChosenPermanentEvent(
                    event.getSource(), 
                    event.getPlayer(), 
                    AN_UNTAPPED_ISLAND_YOU_CONTROL
                ));      
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
