[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Sacrifice an enchantment?"),
                    this,
                    "PN may\$ sacrifice an enchantment. If PN doesn't, sacrifice SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPermanent permanent = event.getPermanent();
            if (player.controlsPermanent(MagicType.Enchantment) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(permanent, player, MagicTargetChoice.SACRIFICE_ENCHANTMENT));
            } else {
                game.doAction(new MagicSacrificeAction(permanent));
            }
        }
    }
]
