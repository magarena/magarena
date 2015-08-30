def choice = Negative("target Wall");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target Wall\$. It can't be regenerated. " + 
                "SN deals damage equal to that Wall's converted mana cost to the Wall's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = it.getConvertedCost();
                game.doAction(ChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(it));
                game.doAction(new DealDamageAction(event.getSource(),it.getController(),amount));
                game.logAppendValue(event.getPlayer(),amount);
            });
        }
    }
]
