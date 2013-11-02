[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature or player\$. " +
                "Scry 1."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->
                game.doAction(new MagicDealDamageAction(
                    new MagicDamage(event.getSource(),target,2)
                ));
                game.doAction(new MagicGainAbilityAction(target, MagicAbility.CannotBlock));
                game.addEvent(new MagicScryEvent(event));
            } as MagicPermanentAction);
        }
    }
]
