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
                    "PN creates a token that's a copy RN. "+
                    "Then if PN controls eight or more artifacts with the same name as one another, PN wins the game."
                ) :
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new PlayTokenAction(player, event.getRefPermanent()));
            final HashMap<String, Integer> names = new HashMap<>();
            ARTIFACT_YOU_CONTROL.filter(player) each {
                final int v = names.containsKey(it.getName()) ? names.get(it.getName()) : 0;
                names.put(it.getName(), v + 1);
            }
            if (!names.isEmpty() && Collections.max(names.values()) >= 8) {
                game.doAction(new LoseGameAction(player.getOpponent()));
            }
        }
    }
]
