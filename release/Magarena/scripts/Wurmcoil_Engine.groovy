[
    new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 3/3 colorless Wurm artifact creature token with deathtouch and "+
                            "a 3/3 colorless Wurm artifact creature token with lifelink onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Wurm1")));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Wurm2")));            
        }
    }
]
