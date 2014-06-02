[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target nonblack creature\$. It can't be regenerated."+
                "If SN was kicked, it deals damage equal to that creature's power to the creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(MagicChangeStateAction.Set(permanent,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(permanent));
                if (event.isKicked()) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),permanent.getController(),permanent.getPower());
                    game.doAction(new MagicDealDamageAction(damage));
                    game.logAppendMessage(event.getPlayer(),"("+permanent.getPower()+")");
                }
            });
        }
    }
]
