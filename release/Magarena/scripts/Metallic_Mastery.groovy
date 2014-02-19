[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_ARTIFACT,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target artifact\$ until end of turn. " +
                "Untap that artifact. It gains haste until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent artifact ->
                game.doAction(new MagicGainControlAction(event.getPlayer(),artifact,MagicStatic.UntilEOT));
                game.doAction(new MagicUntapAction(artifact));
                game.doAction(new MagicGainAbilityAction(artifact,MagicAbility.Haste));
            });
        }
    }
]
