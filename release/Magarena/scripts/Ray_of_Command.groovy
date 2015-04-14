def LoseControlTap = {
    final MagicPlayer you ->
    return new MagicWhenLoseControlTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent target) {
            if (permanent == target && target.getController().getId() != you.getId()) {
                game.addDelayedAction(new RemoveTriggerAction(permanent, this));
                return new MagicEvent(
                    permanent,
                    this,
                    "Tap SN"
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicTapAction(event.getPermanent()));
        }
    };
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                MagicExileTargetPicker.create(),
                this,
                "Untap target creature\$ an opponent controls and gain control of it until end of turn. " + 
                "That creature gains haste until end of turn. When you lose control of the creature, tap it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                game.doAction(new MagicUntapAction(it));
                game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
                game.doAction(new AddTriggerAction(it, LoseControlTap(event.getPlayer())));
            });
        }
    }
]
