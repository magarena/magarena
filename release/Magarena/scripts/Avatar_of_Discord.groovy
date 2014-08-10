[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            return player.getHandSize() > 1 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ discard two cards. " +
                    "If you don't, sacrifice SN."
                ):
                MagicRuleEventAction.create(permanent, "Sacrifice SN.");
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            } else {
                game.addEvent(new MagicDiscardEvent(event.getPermanent(), event.getPlayer(), 2));
            }
        }
    }
]
