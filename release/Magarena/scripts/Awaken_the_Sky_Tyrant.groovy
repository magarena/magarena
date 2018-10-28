[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return permanent.isEnemy(damage.getSource()) && permanent.isController(damage.getTarget()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices SN. If PN does, then create a 5/5 red Dragon creature token with flying."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificeEvent(event.getPermanent());
            if (sac.isSatisfied()) {
                game.addEvent(sac);
                game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("5/5 red Dragon creature token with flying"),1));
            }
        }
    }
]
