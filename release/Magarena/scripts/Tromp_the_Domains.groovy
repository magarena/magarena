[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Until end of turn, creatures you control gain trample and get +1/+1 for each basic land type among lands you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int domain = player.getDomain();
            game.logAppendMessage(player," ("+domain+")");
            final Collection<MagicPermanent> creatures = game.filterPermanents(player,MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicChangeTurnPTAction(creature,domain,domain));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Trample));
            }
        }
    }
]
