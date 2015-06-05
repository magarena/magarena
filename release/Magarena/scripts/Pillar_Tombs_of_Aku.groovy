[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    new MagicMayChoice("Sacrifice a creature?"),
                    this,
                    "PN may\$ sacrifice a creature. If he or she doesn't, PN loses 5 life and "+
                    "${permanent.getController().getName()} sacrifices SN."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicEvent sacrificeEvent = new MagicSacrificePermanentEvent(event.getSource(), player, SACRIFICE_CREATURE)
            if (event.isYes() && sacrificeEvent.isSatisfied()) {
                game.addEvent(sacrificeEvent);
            } else {
                game.doAction(new ChangeLifeAction(player, -5));
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
