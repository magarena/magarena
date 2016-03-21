[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN Bolsters X, where X is the number of cards in PN's hand. "+
                "Each creature PN controls with a +1/+1 counter on it gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getHandSize();
            game.logAppendValue(player,amount);
            int minToughness = Integer.MAX_VALUE;
            CREATURE_YOU_CONTROL.filter(event) each {
                minToughness = Math.min(minToughness, it.getToughnessValue());
            }
            game.addFirstEvent(new MagicBolsterEvent(event.getSource(), player, amount, minToughness));
            CREATURE_PLUSONE_COUNTER_YOU_CONTROL.filter(player) each {
                game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
            }
        }
    }
]
