package emu.lunarcore.data.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import emu.lunarcore.util.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;

/**
 *  Original name: LevelGroupInfo
 */
@Getter
public class GroupInfo {
    private String GroupName;
    private transient int id;
    // 4.6 group files only write LoadSide for server groups; a missing LoadSide means the client
    // spawns the group itself. Sending client groups too left floor 10202001 loading forever
    private GroupLoadSide LoadSide = GroupLoadSide.Client;
    private GroupCategory Category;
    private boolean LoadOnInitial = true;
    private int OwnerMainMissionID;

    private LoadConditionInfo AtmosphereCondition;
    private LoadConditionInfo LoadCondition;
    private LoadConditionInfo UnloadCondition;
    
    private Map<Integer, GroupProperty> GroupPropertyMap;
    
    private List<AnchorInfo> AnchorList;
    private List<MonsterInfo> MonsterList;
    private List<PropInfo> PropList;
    private List<NpcInfo> NPCList;

    private RandomInitInfo RandomInit;

    public void setId(int id) {
        if (this.id == 0) this.id = id;
    }

    /**
     * Picks one weighted set of npcs and props for each control id in RandomInit (crowd groups).
     * The client only waits for the npcs of the props it is sent, so the picked sets must match.
     * @param controlled Filled with every npc/prop id that some set controls
     * @param picked Filled with the npc/prop ids of the picked sets
     */
    public void pickRandomInit(IntSet controlled, IntSet picked) {
        if (this.RandomInit == null || this.RandomInit.getControlList() == null) {
            return;
        }

        var byControlId = new Int2ObjectOpenHashMap<List<RandomInitControl>>();

        for (var control : this.RandomInit.getControlList()) {
            byControlId.computeIfAbsent(control.getControlID(), i -> new ArrayList<>()).add(control);
            control.addIds(controlled);
        }

        for (var controls : byControlId.values()) {
            int total = controls.stream().mapToInt(RandomInitControl::getWeightOrOne).sum();
            int roll = Utils.randomRange(1, total);

            for (var control : controls) {
                roll -= control.getWeightOrOne();
                if (roll <= 0) {
                    control.addIds(picked);
                    break;
                }
            }
        }
    }
    
    public String getGroupName() {
        return this.GroupName != null ? this.GroupName : "" ;
    }
    
    public MonsterInfo getMonsterById(int configId) {
        return MonsterList.stream().filter(m -> m.getID() == configId).findFirst().orElse(null);
    }
    
    @Getter
    public static class GroupProperty {
        private int ID;
        private String Name;
        private String Side; // Enum
        private int DefaultValue;
        private int MaxValue;
    }
    
    @Getter
    public static class RandomInitInfo {
        private List<RandomInitControl> ControlList;
    }

    @Getter
    public static class RandomInitControl {
        private int ControlID;
        private int Weight;
        private List<Integer> NPCIDList;
        private List<Integer> PropIDList;

        public int getWeightOrOne() {
            return Math.max(this.Weight, 1);
        }

        private void addIds(IntSet set) {
            if (this.NPCIDList != null) set.addAll(this.NPCIDList);
            if (this.PropIDList != null) set.addAll(this.PropIDList);
        }
    }

    public static enum GroupLoadSide {
        Client, Server;
    }

    public static enum GroupCategory {
        Normal, Mission, BattleProps, BattleAudiences, Custom, System, Atmosphere;
    }
}
