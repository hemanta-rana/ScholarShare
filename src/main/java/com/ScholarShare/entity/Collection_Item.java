package com.ScholarShare.entity;


import java.sql.Timestamp;

public class Collection_Item {
    private int item_id;
    private int collection_id;
    private int resource_id;
    private Timestamp added_at  ;


    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(int collection_id) {
        this.collection_id = collection_id;
    }

    public Timestamp getAdded_at() {
        return added_at;
    }

    public void setAdded_at(Timestamp added_at) {
        this.added_at = added_at;
    }
    public int getResource_id() {
        return resource_id;
    }
    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }
}
