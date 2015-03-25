[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            for (final MagicCard card : upkeepPlayer.getLibrary().getCardsFromTop(1)) {
                game.doAction(new MagicLookAction(card, upkeepPlayer, "top card of your library"));
                    for (final MagicSubType subtype : MagicSubType.values()) {
                    return card.hasSubType(subtype) == permanent.hasSubType(subtype) ?
                        new MagicEvent(
                            permanent,
                            new MagicMayChoice(),
                            card,
                            this,
                            "PN may\$ reveal the top card of PN's library. If you do, each creature you control gains flying until end of turn."
                        ):
                        MagicEvent.NONE;
                    }
                }
            }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicRevealAction(event.getRefCard()));
                final Collection<MagicPermanent> targets = event.getPlayer().filterPermanents(MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
                    for (final MagicPermanent target : targets) {
                        game.doAction(new MagicGainAbilityAction(target,MagicAbility.Flying));
                }
            }
        }
    }
]
