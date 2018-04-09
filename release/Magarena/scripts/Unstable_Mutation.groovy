[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return enchanted.isValid() ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a -1/-1 counter on creature enchanted by SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent enchanted=permanent.getEnchantedPermanent();
            if (enchanted.isValid()) {
                game.doAction(new ChangeCountersAction(event.getPlayer(),enchanted,MagicCounterType.MinusOne,1));
            }
        }
    }
]
