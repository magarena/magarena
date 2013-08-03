name=Regal Force
url=http://magiccards.info/eve/en/74.html
image=http://magiccards.info/scans/en/eve/74.jpg
value=0
rarity=R
type=Creature
subtype=Elemental
cost={4}{G}{G}{G}
pt=5/5
timing=main
requires_groovy_code
mike@asp12$~/workspace/MagarenaAI/release/Magarena>cat /var/projects/app1/MagarenaScripts/Regal_Force.groovy 
[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card for each green creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = game.filterPermanents(player,MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL);
            game.doAction(new MagicDrawAction(
                player,
                targets.size()
            ));

        }
    }
]
