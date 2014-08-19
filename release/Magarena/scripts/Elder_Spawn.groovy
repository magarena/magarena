[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice an Island?"),
                this,
                "PN may\$ sacrifice an Island. If PN doesn't, sacrifice SN and it deals 6 damage to you."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),new MagicTargetChoice("an Island to sacrifice"));
            if (event.isYes() && sac.hasOptions(game)) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),6);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
