[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact or enchantment\$. PN gains life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),permanent.getConvertedCost()));
            });
        }
    }
]
