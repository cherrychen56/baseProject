package com.magic_chen_.baseproject;

import java.util.Objects;

public class AdvertiseModel {
   public String group;
   public int id;
   public String img_url;
   public String nn_url;
   public int pay_status;
   public int round_time;
   public String sub_title;
   public String title;
   public String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdvertiseModel that = (AdvertiseModel) o;

        if (id != that.id) return false;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdvertiseModel{" +
                "group='" + group + '\'' +
                ", id=" + id +
                ", img_url='" + img_url + '\'' +
                ", nn_url='" + nn_url + '\'' +
                ", pay_status=" + pay_status +
                ", round_time=" + round_time +
                ", sub_title='" + sub_title + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
