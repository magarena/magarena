package magic.model;

import magic.data.MagicIcon;
import magic.data.TextImages;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicManaCostCondition;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MagicManaCost {

    private static final Map<String,MagicManaCost> COSTS_MAP=new HashMap<>();
    private static final Map<String,MagicCondition> CONDS_MAP=new HashMap<>();

    private static final Pattern PATTERN=Pattern.compile("\\{[A-Z\\d/]+\\}");

    private static final int[] SINGLE_PENALTY={0,1,1,3,6,9,12,15,18};
    private static final int[] DOUBLE_PENALTY={0,0,1,2,4,6, 8,10,12};

    private static final MagicIcon[] GENERIC_ICONS={
        MagicIcon.MANA_0,
        MagicIcon.MANA_1,
        MagicIcon.MANA_2,
        MagicIcon.MANA_3,
        MagicIcon.MANA_4,
        MagicIcon.MANA_5,
        MagicIcon.MANA_6,
        MagicIcon.MANA_7,
        MagicIcon.MANA_8,
        MagicIcon.MANA_9,
        MagicIcon.MANA_10,
        MagicIcon.MANA_11,
        MagicIcon.MANA_12,
        MagicIcon.MANA_13,
        MagicIcon.MANA_14,
        MagicIcon.MANA_15,
        MagicIcon.MANA_16
    };

    public static final MagicManaCost ZERO = MagicManaCost.create("{0}");
    public static final MagicManaCost NONE = new MagicManaCost(new int[MagicCostManaType.NR_OF_TYPES], 0);
    public static final int MAXIMUM_MANA_COST = 16;

    private final String costText;
    private final int[] amounts = new int[MagicCostManaType.NR_OF_TYPES];
    private final int converted;
    private final int XCount;
    private final List<MagicCostManaType> order = new ArrayList<>();
    private MagicBuilderManaCost builderCost;
    private List<MagicIcon> icons;

    private MagicManaCost(final String aCostText) {
        costText = aCostText;

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

    private MagicManaCost(final int[] aAmounts, final int aXCount) {
        int total = 0;
        for (int i = 0; i < aAmounts.length; i++) {
            amounts[i] = aAmounts[i];
            total += amounts[i];
            if (amounts[i] != 0) {
                order.add(MagicCostManaType.values()[i]);
            }
        }
        XCount = aXCount;
        converted = total;
        costText = getCanonicalText(amounts, XCount);
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
            addType(MagicCostManaType.Generic,Integer.parseInt(symbol),convertedArr);
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
        int generic=x * XCount;

        for (final MagicCostManaType type : order) {
            int amount=amounts[type.ordinal()];
            if (type == MagicCostManaType.Generic) {
                generic+=amount;
            } else {
                for (;amount>0;amount--) {
                    types.add(type);
                }
            }
        }

        for (;generic>0;generic--) {
            types.add(MagicCostManaType.Generic);
        }

        return types;
    }

    public int getDevotion(final MagicColor... colors) {
        int devotion = 0;
        for (final MagicCostManaType mt : getCostManaTypes(0)) {
            if (mt == MagicCostManaType.Generic) {
                continue;
            }
            for (final MagicColor c : colors) {
                if (mt.getTypes().contains(c.getManaType())) {
                    devotion++;
                    break;
                }
            }
        }
        return devotion;
    }

    public int getColorFlags() {
        int colorFlags = 0;
        for (final MagicCostManaType costType : order) {
            if (costType != MagicCostManaType.Generic &&
                costType != MagicCostManaType.Colorless) {
                for (final MagicManaType manaType : costType.getTypes()) {
                    colorFlags |= manaType.getColor().getMask();
                }
            }
        }
        return colorFlags;
    }

    private static String getCanonicalText(final int[] amounts, final int XCount) {
        final StringBuilder sb = new StringBuilder();
        //add X
        for (int i = 0; i < XCount; i++) {
            sb.append('{').append('X').append('}');
        }
        //add others
        for (final MagicCostManaType type : getCanonicalOrder(amounts)) {
            final int amt = amounts[type.ordinal()];
            if (type == MagicCostManaType.Generic && amt > 0) {
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

    private static List<MagicCostManaType> getCanonicalOrder(final int[] amounts) {
        final List<MagicCostManaType> manaOrder = new ArrayList<>();
        for (final MagicCostManaType type : MagicCostManaType.NON_MONO) {
            final int amt = amounts[type.ordinal()];
            if (amt > 0) {
                manaOrder.add(type);
            }
        }

        //add mono
        MagicCostManaType curr = findFirstMonoSymbol(amounts);
        for (int i = 0; i < 5; i++, curr = curr.next()) {
            final int amt = amounts[curr.ordinal()];
            if (amt > 0) {
                manaOrder.add(curr);
            }
        }

        return manaOrder;
    }

    //find the first mono color in the mana cost order
    private static MagicCostManaType findFirstMonoSymbol(final int[] amounts) {
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
        for (int x = XCount; x > 0; x--) {
            icons.add(MagicIcon.MANA_X);
        }
        for (final MagicCostManaType type : order) {
            int amount = amounts[type.ordinal()];
            if (type == MagicCostManaType.Generic) {
                while (amount > 16) {
                    icons.add(GENERIC_ICONS[16]);
                    amount-=16;
                }
                if (amount >= 0) {
                    icons.add(GENERIC_ICONS[amount]);
                }
            } else {
                final MagicIcon icon = TextImages.getIcon(type.getText());
                for (int a=amount;a>0;a--) {
                    icons.add(icon);
                }
            }
        }
    }

    public List<MagicIcon> getIcons() {
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
            if (type == MagicCostManaType.Generic || amount == 0) {
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

    public int getConvertedCost(final int x) {
        return converted + x * XCount;
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
            if (type == MagicCostManaType.Generic) {
                //skip
            } else {
                aBuilderCost.addType(type,amounts[type.ordinal()]);
            }
        }
        final int generic = Math.max(0, amounts[MagicCostManaType.Generic.ordinal()] + x * XCount);
        aBuilderCost.addType(MagicCostManaType.Generic, generic);
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

    public MagicManaCost reduce(final MagicCostManaType type, final int amt) {
        return increase(type, -amt);
    }

    public MagicManaCost reduce(final int amt) {
        return increase(MagicCostManaType.Generic, -amt);
    }

    public MagicManaCost increase(final int amt) {
        return increase(MagicCostManaType.Generic, amt);
    }

    public MagicManaCost increase(final MagicCostManaType type, final int amt) {
        if (amt == 0) {
            return this;
        }
        final int[] reducedAmounts = Arrays.copyOf(amounts, amounts.length);
        final int idx = type.ordinal();
        reducedAmounts[idx] += amt;
        if (XCount > 0 && type == MagicCostManaType.Generic && reducedAmounts[idx] < 0) {
            return new MagicManaCost(reducedAmounts, XCount);
        } else if (amounts[idx] == 0 && reducedAmounts[idx] < 0) {
            return this;
        } else {
            return MagicManaCost.create(getCanonicalText(reducedAmounts, XCount));
        }
    }
}
