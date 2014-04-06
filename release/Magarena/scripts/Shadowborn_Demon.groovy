[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final Collection<MagicCard> graveyardCreatures = game.filterCards(upkeepPlayer,MagicTargetFilterFactory.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
            return permanent.isController(upkeepPlayer) && graveyardCreatures.size()<6 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices a creature"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),MagicTargetChoice.SACRIFICE_CREATURE));
        }
    }
]
