[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land\$, then SN deals damage to that land's controller "+
                "equal to the number of land cards in that player's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
                final int amount = game.filterCards(permanent.getController(),MagicTargetFilterFactory.LAND_CARD_FROM_YOUR_GRAVEYARD).size();
                final MagicDamage damage=new MagicDamage(event.getSource(),permanent.getController(),amount);
                game.doAction(new MagicDealDamageAction(damage));
                game.logAppendMessage(event.getPlayer(),"("+amount+")");
            });
        }
    }
]
