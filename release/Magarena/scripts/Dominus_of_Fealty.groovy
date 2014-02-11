[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.NEG_TARGET_PERMANENT),
					MagicExileTargetPicker.create(),
                    this,
                    "Gain control of target creature\$ until end of turn. Untap it. " +
					"It gains haste until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
					final MagicPermanent permanent ->
					game.doAction(new MagicGainControlAction(event.getPlayer(),permanent,MagicStatic.UntilEOT));
					game.doAction(new MagicUntapAction(permanent));
					game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.Haste));
				});
            }
        }
    }
]
