[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target creature\$. It can't be regenerated. "+
                "PN loses life equal to that creature's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(creature));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-creature.getToughness()));
            });
        }
    }
]
