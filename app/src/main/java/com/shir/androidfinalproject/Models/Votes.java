package com.shir.androidfinalproject.Models;

public class Votes {
    private int votesCount;

     public Votes() {
        this.Clear();
    }

    public int getVotesCount() {
        return votesCount;
    }

    public void AddVote() {
        this.votesCount ++;
    }

    public void RemoveVote() {
        if (this.votesCount > 0){
            this.votesCount--;
        }
    }

    public void Clear(){
        this.votesCount = 0;
    }
}
