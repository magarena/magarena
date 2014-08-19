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
            final MagicEvent sac = new MagicSacrificePermanentEvent(
                event.getPermanent(),
                event.getPlayer(),
                MagicTargetChoice.SACRIFICE_ARTIFACT
            );
            if (event.isYes() && sac.hasOptions(game)) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicTapAction(event.getPermanent()));
                final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),2)
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
