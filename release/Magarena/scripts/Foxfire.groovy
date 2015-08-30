[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicTargetChoice("target attacking creature"),
                MagicPreventTargetPicker.create(),     
                this,
                "Untap target attacking creature\$. " + 
                "Prevent all combat damage that would be dealt to and dealt by that creature this turn. " +
                "Draw a card at the beginning of the next turn's upkeep."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new UntapAction(it));
                game.doAction(new AddTurnTriggerAction(
                    it,
                    MagicPreventDamageTrigger.PreventCombatDamageDealtToDealtBy
                ));
                game.doAction(new AddTriggerAction(
                    MagicAtUpkeepTrigger.YouDraw(
                        event.getSource(), 
                        event.getPlayer()
                    )
                ));
            });
        }
    }
]
