def ENCHANTMENT_YOU_DONT_CONTROL = new MagicPermanentFilterImpl () {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasType(MagicType.Enchantment) && target.isController(player) == false;
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all enchantments PN doesn't control. Then, if PN controls an untapped land, "+
                "destroy all enchantments PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            ENCHANTMENT_YOU_DONT_CONTROL.filter(player) each {
                game.doAction(new DestroyAction(it));
            }
            if (player.controlsPermanent(UNTAPPED_LAND)) {
                ENCHANTMENT_YOU_CONTROL.filter(player) each {
                    game.doAction(new DestroyAction(it));
                }
            }
        }
    }
]
