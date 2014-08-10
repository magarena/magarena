[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            final MagicPermanent enchantedCreature=permanent.getEnchantedPermanent();
            return permanent.isController(player) && enchantedCreature.getPower()>=4 ?
                MagicRuleEventAction.create(permanent, "Sacrifice SN."):
                MagicEvent.NONE;
        }
    }
]
