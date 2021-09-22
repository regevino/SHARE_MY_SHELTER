package com.huji_postpc_avih.sharemyshelter.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SheltersHolder {

    ArrayList<Shelter> shelters;

    public SheltersHolder(ArrayList<Shelter> shelters) {
        this.shelters = shelters;
    }

    public void deleteShelter(Shelter shelterToDel) {
        for (int i = 0, sheltersSize = shelters.size(); i < sheltersSize; i++) {
            Shelter shelter = shelters.get(i);
            if (shelterToDel.getId().equals(shelter.getId())) {
                shelters.remove(i);
            }
        }
    }

    public void deleteShelter(UUID shelterId) {
        for (int i = 0, sheltersSize = shelters.size(); i < sheltersSize; i++) {
            Shelter shelter = shelters.get(i);
            if (shelterId.equals(shelter.getId())) {
                shelters.remove(i);
            }
        }
    }

    public void addShelter(Shelter shelter) {
        shelters.add(shelter);
    }

    public List<Shelter> getItemsList() {
        return shelters;
    }


}
