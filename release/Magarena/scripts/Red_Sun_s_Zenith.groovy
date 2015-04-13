[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                amount,
                this,
                "SN deals RN damage to target creature or player.\$ " +
                "If a creature dealt damage this way would die this turn, exile it instead. "+
                "Shuffle SN into its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            int dealtDamage = 0;
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,amount);
                game.doAction(new MagicDealDamageAction(damage));
                dealtDamage = damage.getDealtAmount();
                game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersLibrary));
            });
            event.processTargetPermanent(game, {
                if (dealtDamage > 0) {
                    game.doAction(new AddTurnTriggerAction(
                        it,
                        MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead
                    ));
                }
            });
        }
    }
]
