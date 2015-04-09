[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            return (tapped.isFriend(permanent) &&
                    tapped.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_POS_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a quest counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Quest,1));
            }
        }
    },
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isOpponent(upkeepPlayer) &&
                    permanent.getCounters(MagicCounterType.Quest) >= 4) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap all creatures you control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(player,CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicUntapAction(target));
            }
        }
    }
]
