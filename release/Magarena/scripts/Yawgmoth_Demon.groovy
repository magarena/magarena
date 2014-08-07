[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Sacrifice an artifact?"),
                    this,
                    "PN may\$ sacrifice an artifact. If PN doesn't, tap SN and it deals 2 damage to you."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicType.Artifact) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),MagicTargetChoice.SACRIFICE_ARTIFACT));
            } else {
                game.doAction(new MagicTapAction(event.getPermanent(),true));
		final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),2)
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
