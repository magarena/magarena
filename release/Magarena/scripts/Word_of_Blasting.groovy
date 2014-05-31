[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.Negative("Target Wall"),
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target Wall\$. It can't be regenerated. SN deals damage equal to that Wall's converted mana cost. "+
                "to the Wall's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(MagicChangeStateAction.Set(permanent,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(permanent));
                final MagicDamage damage=new MagicDamage(event.getSource(),permanent.getController(),permanent.getConvertedCost());
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
