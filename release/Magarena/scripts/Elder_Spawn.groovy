[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Sacrifice an Island?"),
                    this,
                    "PN may\$ sacrifice an Island. If PN doesn't, sacrifice SN and it deals 6 damage to you."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Island) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),new MagicTargetChoice("an Island to sacrifice")));
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),6);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
