package magic.model;

import magic.ui.IconImages;
import magic.data.TextImages;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicManaCostCondition;

import javax.swing.ImageIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import magic.data.MagicIcon;

public class MagicManaCost {

    private static final Map<String,MagicManaCost> COSTS_MAP=new HashMap<>();
    private static final Map<String,MagicCondition> CONDS_MAP=new HashMap<>();

    private static final Pattern PATTERN=Pattern.compile("\\{[A-Z\\d/]+\\}");

    private static final int[] SINGLE_PENALTY={0,1,1,3,6,9,12};
    private static final int[] DOUBLE_PENALTY={0,0,1,2,4,6,8};

    private static final ImageIcon[] COLORLESS_ICONS={
        IconImages.getSmallManaIcon(MagicIcon.ZERO),
        IconImages.getSmallManaIcon(MagicIcon.ONE),
        IconImages.getSmallManaIcon(MagicIcon.TWO),
        IconImages.getSmallManaIcon(MagicIcon.THREE),
        IconImages.getSmallManaIcon(MagicIcon.FOUR),
        IconImages.getSmallManaIcon(MagicIcon.FIVE),
        IconImages.getSmallManaIcon(MagicIcon.SIX),
        IconImages.getSmallManaIcon(MagicIcon.SEVEN),
        IconImages.getSmallManaIcon(MagicIcon.EIGHT),
        IconImages.getSmallManaIcon(MagicIcon.NINE),
        IconImages.getSmallManaIcon(MagicIcon.TEN),
        IconImages.getSmallManaIcon(MagicIcon.ELEVEN),
        IconImages.getSmallManaIcon(MagicIcon.TWELVE),
        IconImages.getSmallManaIcon(MagicIcon.THIRTEEN),
        IconImages.getSmallManaIcon(MagicIcon.FOURTEEN),
        IconImages.getSmallManaIcon(MagicIcon.FIFTEEN),
        IconImages.getSmallManaIcon(MagicIcon.SIXTEEN)
    };

    public static final MagicManaCost ZERO=MagicManaCost.create("{0}");

    private final String costText;
    private final int[] amounts;
    private final int converted;
    private final int XCount;
    private final List<MagicCostManaType> order;
    private MagicBuilderManaCost builderCost;
    private List<ImageIcon> icons;

    private MagicManaCost(final String aCostText) {
        costText = aCostText;
        amounts = new int[MagicCostManaType.NR_OF_TYPES];
        order = new ArrayList<>();

        final int[] XCountArr = {0};
        final int[] convertedArr = {0};

        final Matcher matcher = PATTERN.matcher(costText);
        int matched = 0;
        while (matcher.find()) {
            final String group = matcher.group();
            matched += group.length();
            addType(group, XCountArr, convertedArr);
        }

        if (matched != costText.length()) {
            throw new RuntimeException("unknown mana cost \"" + aCostText + "\"");
        }

        XCount = XCountArr[0];
        converted = convertedArr[0];

        //assert getCanonicalText().equals(costText) : "canonical: " + getCanonicalText() + " != cost: " + costText;
    }

    private void addType(final MagicCostManaType type,final int amount,final int[] convertedArr) {
        convertedArr[0] += amount;
        amounts[type.ordinal()] += amount;
        if (!order.contains(type)) {
            order.add(type);
        }
    }

    private void addType(final String typeText, final int[] XCountArr, final int[] convertedArr) {
        final String symbol = typeText.substring(1, typeText.length() - 1);
        if ("X".equals(symbol)) {
            XCountArr[0]++;
        } else if (isNumeric(symbol)) {
            addType(MagicCostManaType.Colorless,Integer.parseInt(symbol),convertedArr);
        } else {
            for (final MagicCostManaType type : MagicCostManaType.values()) {
                if (type.getText().equals(typeText)) {
                    addType(type,1,convertedArr);
                    return;
                }
            }
            throw new RuntimeException("unknown mana cost \"" + costText + "\"");
        }
    }

    private static boolean isNumeric(final String str) {
        for (final char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public String getText() {
        return costText;
    }

    @Override
    public String toString() {
        return costText;
    }

    public int getXCount() {
        return XCount;
    }
    public boolean hasX() {
        return XCount > 0;
    }

    public List<MagicCostManaType> getCostManaTypes(final int x) {
        final List<MagicCostManaType> types = new ArrayList<>();
        int colorless=x;

        for (final MagicCostManaType type : order) {
            int amount=amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless) {
                colorless+=amount;
            } else {
                for (;amount>0;amount--) {
                    types.add(type);
                }
            }
        }

        for (;colorless>0;colorless--) {
            types.add(MagicCostManaType.Colorless);
        }

        return types;
    }

    public int getColorFlags() {
        int colorFlags = 0;
        for (final MagicCostManaType costType : order) {
            if (costType != MagicCostManaType.Colorless) {
                for (final MagicManaType manaType : costType.getTypes()) {
                    colorFlags |= manaType.getColor().getMask();
                }
            }
        }
        return colorFlags;
    }

    private String getCanonicalText() {
        final StringBuilder sb = new StringBuilder();
        //add X
        for (int i = 0; i < XCount; i++) {
            sb.append('{').append('X').append('}');
        }
        //add others
        for (final MagicCostManaType type : getCanonicalOrder()) {
            final int amt = amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless && amt > 0) {
                sb.append('{').append(amt).append('}');
                continue;
            }
            for (int i = 0; i < amt ; i++) {
                sb.append(type.getText());
            }
        }
        if (sb.length() == 0) {
            return "{0}";
        } else {
            return sb.toString();
        }
    }

    private List<MagicCostManaType> getCanonicalOrder() {
        final List<MagicCostManaType> manaOrder = new ArrayList<>();
        for (final MagicCostManaType type : MagicCostManaType.NON_MONO) {
            final int amt = amounts[type.ordinal()];
            if (amt > 0) {
                manaOrder.add(type);
            }
        }

        //add mono
        MagicCostManaType curr = findFirstMonoSymbol();
        for (int i = 0; i < 5; i++, curr = curr.next()) {
            final int amt = amounts[curr.ordinal()];
            if (amt > 0) {
                manaOrder.add(curr);
            }
        }

        return manaOrder;
    }

    //find the first mono color in the mana cost order
    private MagicCostManaType findFirstMonoSymbol() {
        //keep color with amount > 0 and has no prev
        final List<MagicCostManaType> cand = new ArrayList<>();
        for (final MagicCostManaType color : MagicCostManaType.MONO) {
            final int amt_c = amounts[color.ordinal()];
            final int amt_p = amounts[color.prev().ordinal()];
            if (amt_c > 0 && amt_p == 0) {
                cand.add(color);
            }
        }

        if (cand.isEmpty()) {
            //WUBRG
            return MagicCostManaType.White;
        } else if (cand.size() == 1) {
            //Linear chain
            return cand.get(0);
        } else {
            //cand.size() == 2
            //Wedge
            final MagicCostManaType t1 = cand.get(0);
            final MagicCostManaType t2 = cand.get(1);
            return (t1.prev().prev() == t2) ? t2 : t1;
        }
    }

    private void buildIcons() {
        for (int x=XCount;x>0;x--) {
            icons.add(IconImages.getSmallManaIcon(MagicIcon.X));
        }
        for (final MagicCostManaType type : order) {
            int amount = amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless) {
                while (amount > 16) {
                    icons.add(COLORLESS_ICONS[16]);
                    amount-=16;
                }
                if (amount >= 0) {
                    icons.add(COLORLESS_ICONS[amount]);
                }
            } else {
                final ImageIcon icon = TextImages.getIcon(type.getText());
                for (int a=amount;a>0;a--) {
                    icons.add(icon);
                }
            }
        }
    }

    public List<ImageIcon> getIcons() {
        if (icons == null) {
            icons = new ArrayList<>();
            buildIcons();
        }
        return icons;
    }

    int getCostScore(final MagicDeckProfile profile) {
        final int[] singleCounts=new int[MagicManaType.NR_OF_TYPES];
        int doubleCount=0;
        int maxSingleCount=0;
        for (final MagicCostManaType type : order) {
            final int amount = amounts[type.ordinal()];
            if (type == MagicCostManaType.Colorless || amount == 0) {
                continue;
            }
            final MagicManaType[] profileTypes = type.getTypes(profile);
            switch (profileTypes.length) {
                case 0:
                    return 0;
                case 1:
                    final int typeIndex=profileTypes[0].ordinal();
                    singleCounts[typeIndex]+=amount;
                    maxSingleCount=Math.max(maxSingleCount,singleCounts[typeIndex]);
                    break;
                case 2:
                    doubleCount+=amount;
                    break;
            }
        }
        return 2*converted+3*(10-SINGLE_PENALTY[maxSingleCount]-DOUBLE_PENALTY[doubleCount]);
    }

    public int getConvertedCost() {
        return converted;
    }

    public MagicBuilderManaCost getBuilderCost() {
        if (builderCost == null) {
            builderCost=new MagicBuilderManaCost();
            addTo(builderCost);
        }
        return builderCost;
    }

    public void addTo(final MagicBuilderManaCost aBuilderCost) {
        for (final MagicCostManaType type : order) {
            aBuilderCost.addType(type,amounts[type.ordinal()]);
        }
        if (hasX()) {
            aBuilderCost.setXCount(XCount);
        }
        aBuilderCost.compress();
    }

    public void addTo(final MagicBuilderManaCost aBuilderCost,final int x) {
        for (final MagicCostManaType type : order) {
            aBuilderCost.addType(type,amounts[type.ordinal()]);
        }
        aBuilderCost.addType(MagicCostManaType.Colorless,x);
        aBuilderCost.compress();
    }

    public MagicCondition getCondition() {
        MagicCondition cond = CONDS_MAP.get(costText);
        if (cond == null) {
            cond = new MagicManaCostCondition(this);
            CONDS_MAP.put(costText, cond);
        }
        return cond;
    }

    public static MagicManaCost create(final String costText) {
        MagicManaCost cost = COSTS_MAP.get(costText);
        if (cost == null) {
            cost = new MagicManaCost(costText);
            COSTS_MAP.put(costText,cost);
        }
        return cost;
    }
}
