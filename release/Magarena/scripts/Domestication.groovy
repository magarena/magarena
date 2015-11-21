def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            final MagicPermanent enchantedCreature=permanent.getEnchantedPermanent();
            return permanent.isController(player) && enchantedCreature.getPower()>=4 ?
                EFFECT.getEvent(permanent) :
                MagicEvent.NONE;
        }
    }
]
