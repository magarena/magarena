[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isEnchanted() ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 1/1 green Saproling creature token onto the battlefield."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("1/1 green Saproling creature token")));
        }
    }
]
