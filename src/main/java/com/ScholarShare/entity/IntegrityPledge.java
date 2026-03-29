package com.ScholarShare.entity;

import java.sql.Timestamp;
// map to integrity_pledges
public class IntegrityPledge {
    private int pledgeId;
    private int userId;
    private boolean agreed;
    private Timestamp agreedAt;

    public IntegrityPledge(){}

    public int getPledgeId() {
        return pledgeId;
    }

    public void setPledgeId(int pledgeId) {
        this.pledgeId = pledgeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public Timestamp getAgreedAt() {
        return agreedAt;
    }

    public void setAgreedAt(Timestamp agreedAt) {
        this.agreedAt = agreedAt;
    }
}
