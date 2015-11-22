[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Prevent all combat damage that would be dealt by target creature\$ this turn. "+
                "That creature gets +0/+X until end of turn, where X is its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                final int amount = permanent.getConvertedCost();
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new AddTurnTriggerAction(
                    permanent,
                    PreventDamageTrigger.PreventCombatDamageDealtBy
                ));
                game.doAction(new ChangeTurnPTAction(permanent, 0, amount));
            });
        }
    }
]
