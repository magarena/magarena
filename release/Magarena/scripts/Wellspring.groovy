[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getEnchantedPermanent(),
                this,
                "PN untaps RN. PN gains control of that land until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent land = event.getRefPermanent();
            game.doAction(new UntapAction(land));
            game.doAction(new GainControlAction(event.getPlayer(), land, MagicStatic.UntilEOT));
        }
    }
]
