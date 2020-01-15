package magic.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import magic.ai.MagicAIImpl;
import magic.model.player.AiProfile;
import magic.model.player.HumanProfile;

public class MagicMessageTest {
    private DuelPlayerConfig cfgAi = new DuelPlayerConfig(AiProfile.create(MagicAIImpl.MMAB, 12), new MagicDeckProfile("r"));
    private DuelPlayerConfig cfg = new DuelPlayerConfig(HumanProfile.create("myself"), new MagicDeckProfile("r"));
    private MagicPlayer player = new MagicPlayer(20, cfg, 0);
    private MagicPlayer playerAi = new MagicPlayer(20, cfgAi, 0);

    @Test
    public void testNameReplacement() {
        MagicCardDefinition def = new MagicCardDefinition();
        def.setName("Card Name");
        MagicCardDefinition defRef = new MagicCardDefinition();
        defRef.setName("Recard Name");
        MagicCard source = new MagicCard(def, player, 1);
        MagicCard ref = new MagicCard(defRef, player, 2);
        MagicInteger numRef = new MagicInteger(7);
        assertEquals("Sacrifice <Card Name~1>",
            MagicMessage.replaceName("Sacrifice SN", source, player, ref));
        assertEquals("myself draws 7 cards",
            MagicMessage.replaceName("PN draws RN cards", source, player, numRef));
        assertEquals("myself deals 1 damage to <Recard Name~2>",
            MagicMessage.replaceName("PN deals 1 damage to RN", source, player, ref));
        assertEquals("Pay 7 life",
            MagicMessage.replaceName("Pay RN life", source, player, numRef));
        assertEquals("MMAB has no cards to discard",
            MagicMessage.replaceName("PN has no cards to discard", source, playerAi, ref));
        assertEquals("Target player$ draws X cards, where X is the number of lands.",
            MagicMessage.replaceName("Target player$ draws X cards, where X is the number of lands.", source, player, ref));
        assertEquals("Pay {X}{G}$",
            MagicMessage.replaceName("Pay {X}{G}$", source, playerAi, ref));
    }
}
