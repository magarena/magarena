[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return enchanted.isValid() ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a token that's a copy of enchanted creature onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent enchanted=permanent.getEnchantedPermanent();
            if (enchanted.isValid()) {
                game.doAction(new PlayTokenAction(event.getPlayer(),enchanted));
            }
        }
    }
]
