[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = 0;
            WOLF_OR_WEREWOLF_YOU_CONTROL.filter(cardOnStack.getController()) each {
                amount += it.getPower();
            };
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                new MagicDamageTargetPicker(amount),
                this,
                "PN chooses target creature he or she don't control.\$ Each creature PN control's "+
                "that's a Wolf or a Werewolf deals damage equal to its power to that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final Collection<MagicPermanent> creatures = WOLF_OR_WEREWOLF_YOU_CONTROL.filter(event);
                for (final MagicPermanent creature : creatures) {
                    game.logAppendMessage(event.getPlayer(), String.format(
                        "%s deals (${creature.getPower()}) damage to %s",
                        MagicMessage.getCardToken(creature),
                        MagicMessage.getCardToken(it)
                    ));
                    game.doAction(new DealDamageAction(creature, it, creature.getPower()));
                }
            });
        }
    }
]
