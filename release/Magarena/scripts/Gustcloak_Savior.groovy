[
    new BecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.isFriend(creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    creature,
                    this,
                    "PN may\$ untap RN and remove it from combat."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPermanent permanent = event.getRefPermanent();
                game.doAction(new UntapAction(permanent));
                game.doAction(new RemoveFromCombatAction(permanent));
            }
        }
    }
]
