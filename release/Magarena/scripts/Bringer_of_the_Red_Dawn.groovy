[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(MagicTargetChoice.TARGET_CREATURE),
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
                    game.doAction(new MagicGainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                    game.doAction(new MagicUntapAction(it));
                    game.doAction(new MagicGainAbilityAction(it,MagicAbility.Haste));
                });
            }
        }
    }
]
