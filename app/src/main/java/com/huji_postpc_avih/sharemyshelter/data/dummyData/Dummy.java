package com.huji_postpc_avih.sharemyshelter.data.dummyData;

import android.content.Context;
import android.location.Location;

import com.huji_postpc_avih.sharemyshelter.data.Shelter;

public class Dummy {

    public static Shelter getDummyShelter(Context c)
    {
        return new Shelter(new Location(""), "Dummy shelter", Shelter.ShelterType.PUBLIC, null);
    }

}
