def choice = MagicTargetChoice.Negative("target Wall");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target Wall\$. It can't be regenerated. SN deals damage equal to that Wall's converted mana cost. "+
                "to the Wall's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(MagicChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(it));
                final MagicDamage damage=new MagicDamage(event.getSource(),it.getController(),it.getConvertedCost());
                game.doAction(new MagicDealDamageAction(damage));
                game.logAppendMessage(event.getPlayer(),"("+it.getConvertedCost()+")");
            });
        }
    }
]
