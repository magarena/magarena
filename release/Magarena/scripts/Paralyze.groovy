[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return upkeepPlayer == enchanted.getController() ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice(
                        "Pay {4}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{4}"))
                    ),
                    this,
                    "PN may pay {4}\$. If he or she does, untap ${enchanted}"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new UntapAction(event.getPermanent().getEnchantedPermanent()));
            }
        }
    }
]
