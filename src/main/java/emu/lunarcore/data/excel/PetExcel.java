package emu.lunarcore.data.excel;

import com.google.gson.annotations.SerializedName;

import emu.lunarcore.data.resource.GameResource;
import emu.lunarcore.data.resource.ResourceType;
import lombok.Getter;

@Getter
@ResourceType(name = {"PetConfig.json"})
public class PetExcel extends GameResource {
    // 4.6 obfuscates these column names; values are unchanged from 4.2
    @SerializedName(value = "PetID", alternate = {"PBLDLDIEFNC"})
    private int PetID;
    @SerializedName(value = "PetItemID", alternate = {"HHDHIDNHCBI"})
    private int PetItemID;
    @SerializedName(value = "SummonUnitID", alternate = {"FNBGMDDOHEA"})
    private int SummonUnitID;

    @Override
    public int getId() {
        return PetID;
    }

}
