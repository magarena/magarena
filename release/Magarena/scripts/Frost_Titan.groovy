[
    //counter opponent spell or ability unless its controller pay {2}
    new ThisIsTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return permanent.isEnemy(target) ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Counter spell or ability\$ unless its controller pays {2}."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent mevent) {
            final MagicSource source = mevent.getSource();
            final MagicItemOnStack target = mevent.getRefItemOnStack();
            game.addEvent(new MagicCounterUnlessEvent(source,target,MagicManaCost.create("{2}")));
        }
    }
]
