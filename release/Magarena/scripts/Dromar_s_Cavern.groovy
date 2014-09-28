def NONLAIR_LAND=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasSubType(MagicSubType.Lair) && target.isLand();
    } 
};

def TARGET_NONLAIR_LAND = new MagicTargetChoice(
    NONLAIR_LAND,
    "target nonlair land"
);

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_NONLAIR_LAND),
                this,
                "PN may\$ return a nonlair land to his or her hand. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent();
            if (event.getPlayer().controlsPermanent(NONLAIR_LAND) && event.isYes()) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            });     
            } else {
                game.doAction(new MagicSacrificeAction(perm));
            }
        }
    }
]
