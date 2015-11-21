def choice = new MagicTargetChoice("an Island to sacrifice");

[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice an Island?"),
                this,
                "PN may\$ sacrifice an Island. If PN doesn't, sacrifice SN and it deals 6 damage to him or her."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),choice);
            if (event.isYes() && sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),6));
            }
        }
    }
]
