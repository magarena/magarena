def KNIGHT=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Knight);
    } 
};

def TARGET_KNIGHT = new MagicTargetChoice(
    KNIGHT,
    "target knight"
);

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    "Pay {U}?",
                    new MagicPayManaCostChoice(MagicManaCost.create("{U}")),
                    TARGET_KNIGHT
                ),
                this,
                "PN may\$ pay {U}\$. If PN doesn't, destroy target knight\$. It can't be regenerated."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
            if (event.isNo() && it.isValid()) {
                    game.doAction(MagicChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                    game.doAction(new MagicDestroyAction(it));
                }
            });
        }
    }
]
