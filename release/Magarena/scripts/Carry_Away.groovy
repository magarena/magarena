[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPermanent equipment = permanent.getEnchantedPermanent();
            return equipment.getEquippedCreature().isValid() ?
                new MagicEvent(
                    permanent,
                    equipment,
                    this,
                    "Unattach RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AttachAction(event.getRefPermanent(), MagicPermanent.NONE));
        }
    }
]
