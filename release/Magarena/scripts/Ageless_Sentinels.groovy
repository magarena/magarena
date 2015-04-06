def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.remove(MagicSubType.Wall);
        flags.add(MagicSubType.Bird);
        flags.add(MagicSubType.Giant);
    }
};

def AB = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        flags.remove(MagicAbility.Defender);
    }
};

[
   new MagicWhenBlocksTrigger() {
       @Override
       public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent blocker) {
           return (permanent == blocker) ?
               new MagicEvent(
                   permanent,
                   this,
                   "SN becomes a Bird Giant and loses defender."
               ):
               MagicEvent.NONE;
       }
       @Override
       public void executeEvent(final MagicGame game, final MagicEvent event) {
           final MagicPermanent permanent = event.getPermanent();
           game.doAction(new MagicAddStaticAction(permanent, ST));
           game.doAction(new MagicAddStaticAction(permanent, AB));
       }
   }
]
