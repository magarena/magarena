[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.getController().getNrOfPermanents(MagicSubType.Gate) >= 2 ?
                new MagicEvent(
                    permanent,
                    TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    MagicExileTargetPicker.create(),
                    this,
                    "If PN controls two or more Gates, PN gains control of target creature an opponent controls\$ until end of turn. " +
                    "Untap that creature. It gains haste until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(MagicSubType.Gate) >= 2) {
                event.processTargetPermanent(game, {
                    game.doAction(new GainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                    game.doAction(new MagicUntapAction(it));
                    game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
                });
            }
        }
    }
]
