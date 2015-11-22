def EFFECT = MagicRuleEventAction.create("Sacrifice SN.");

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            return player.getHandSize() > 0 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ discard a card at random. " +
                    "If PN doesn't, sacrifice SN."
                ):
                EFFECT.getEvent(permanent);
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
            } else {
                game.addEvent(MagicDiscardEvent.Random(event.getPermanent(), event.getPlayer(), 1));
            }
        }
    }
]
