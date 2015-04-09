[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final int amt = upkeepPlayer.filterCards(CREATURE_CARD_FROM_GRAVEYARD).size();
            return amt < 6 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If there are fewer than six creature cards in PN'sgraveyard, PN sacrifices a creature"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.getPlayer().filterCards(CREATURE_CARD_FROM_GRAVEYARD).size();
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
