def NONWHITE_ENCHANTMENT=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasColor(MagicColor.White) && target.isEnchantment();
    } 
};

def EFFECT1 = MagicRuleEventAction.create("Destroy all enchantments.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ - destroy all enchantments; " +
                "or destroy all nonwhite enchantments.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(2)) {
                final Collection<MagicPermanent> targets = game.filterPermanents(NONWHITE_ENCHANTMENT);
                game.doAction(new DestroyAction(targets));
            } else {
                event.executeModalEvent(game, EFFECT1);
            }
        }
    }
]
