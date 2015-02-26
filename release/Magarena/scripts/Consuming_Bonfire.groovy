def NON_ELEMENTAL_CREATURE=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasSubType(MagicSubType.Elemental) && target.isCreature();
    } 
};

def TARGET_NON_ELEMENTAL_CREATURE = new MagicTargetChoice(
    NON_ELEMENTAL_CREATURE,
    "target non-Elemental creature"
);

def TREEFOLK_CREATURE=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Treefolk) && target.isCreature();
    } 
};

def TARGET_TREEFOLK_CREATURE = new MagicTargetChoice(
    TREEFOLK_CREATURE,
    "target Treefolk creature"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    TARGET_NON_ELEMENTAL_CREATURE,
                    TARGET_TREEFOLK_CREATURE
                ),
                this,
                "Choose one\$ - SN deals 4 damage to target non-Elemental creature; " +
                "or SN deals 7 damage to target Treefolk creature.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,4))
                });
            } else if (event.isMode(2)) {
            event.processTargetPermanent(game, {
                 game.doAction(new MagicDealDamageAction(event.getSource(),it,7))
                });
            }
        }
    }
]
