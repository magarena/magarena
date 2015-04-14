[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature\$. Untap that creature. " +
                "It gains haste until end of turn. Sacrifice it at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainControlAction(event.getPlayer(),it));
                game.doAction(new MagicUntapAction(it));
                game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
                game.doAction(new AddTriggerAction(it, MagicAtEndOfTurnTrigger.Sacrifice));
            });
        }
    }
]
