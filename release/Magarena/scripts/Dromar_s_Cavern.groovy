def NONLAIR_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasSubType(MagicSubType.Lair) && target.isLand() && target.isController(player);
    } 
};

def A_NONLAIR_LAND_YOU_CONTROL = new MagicTargetChoice(
    NONLAIR_LAND_YOU_CONTROL,
    "a non-Lair land you control"
);

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ return a non-Lair land you control to its owner's hand. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent bounce = new MagicBounceChosenPermanentEvent(event.getSource(), event.getPlayer(), A_NONLAIR_LAND_YOU_CONTROL);
            if (event.isYes() && bounce.isSatisfied()) {
                game.addEvent(bounce);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
