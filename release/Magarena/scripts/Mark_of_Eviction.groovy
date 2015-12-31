[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent()
            return enchanted.isValid() ?
                new MagicEvent(
                    permanent,
                    enchanted,
                    this,
                    "Return RN and all Auras attached to that creature to their owners' hands."
                ) :
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent enchanted = event.getRefPermanent();
            game.doAction(new RemoveAllFromPlayAction(enchanted.getAuraPermanents(), MagicLocationType.OwnersHand));
            game.doAction(new RemoveFromPlayAction(enchanted, MagicLocationType.OwnersHand));
        }
    }
]
