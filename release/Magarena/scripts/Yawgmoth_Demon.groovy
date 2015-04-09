[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice an artifact?"),
                this,
                "PN may\$ sacrifice an artifact. If PN doesn't, tap SN and it deals 2 damage to you."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicEvent sac = new MagicSacrificePermanentEvent(
                event.getPermanent(),
                player,
                SACRIFICE_ARTIFACT
            );
            if (event.isYes() && sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicTapAction(event.getPermanent()));
                game.doAction(new MagicDealDamageAction(event.getSource(),player,2));
            }
        }
    }
]
