[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target creature an opponent controls\$ until end of turn. Untap that creature. " +
                "It gains haste until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicGainControlAction(event.getPlayer(),creature,MagicStatic.UntilEOT));
                game.doAction(new MagicUntapAction(creature));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Haste));
            });
        }
    }
]
