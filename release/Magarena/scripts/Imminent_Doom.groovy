[
//Whenever you cast a spell with converted mana cost equal to the number of doom counters on SN, SN deals that much damage to target creature or player. Then put a doom counter on SN.
    new OtherIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
            return (spell.getConvertedCost() == permanent.getCounters(MagicCounterType.Doom)) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_CREATURE_OR_PLAYER,
                    spell.getConvertedCost(),
                    this,
                    "SN deals RN damage to target creature or player\$. " +
                    "Then put a doom counter on SN."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, event.getRefInt()));
            });
            game.doAction(new ChangeCountersAction(event.getSource(), MagicCounterType.Doom, 1));
        }
    }
]

