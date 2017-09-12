package com.shir.androidfinalproject.Lists;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.shir.androidfinalproject.Models.Votes;

import java.util.ArrayList;

public class VotesList<T extends Votes> extends ArrayList<Votes> {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Votes getMaximom()
    {
        if (this.isEmpty()) {
            return null;
        }
        else {
            return this.stream().max((votes1, votes2) ->
                    Integer.compare(votes1.getVotesCount(), votes2.getVotesCount())).get();
        }
    }
}

