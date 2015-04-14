[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONBLACK_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target nonblack creature\$. It can't be regenerated."+
                "If SN was kicked, it deals damage equal to that creature's power to the creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(ChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(it));
                if (event.isKicked()) {
                    game.doAction(new DealDamageAction(event.getSource(),it.getController(),it.getPower()));
                    game.logAppendMessage(event.getPlayer(),"("+it.getPower()+")");
                }
            });
        }
    }
]
