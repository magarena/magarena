[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final int amt = CREATURE_CARD_FROM_GRAVEYARD.filter(upkeepPlayer).size();
            return amt < 6 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If there are fewer than six creature cards in PN's graveyard, PN sacrifices a creature"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
            if (amt < 6) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    event.getPlayer(),
                    SACRIFICE_CREATURE
                ));
            }
        }
    }
]
