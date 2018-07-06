package com.lukou.publishervideo.model.bean;

/**
 * Created by cxt on 2018/6/14.
 */

public class PublisherVideo {
    private String updateTime;
    private String publisherName;
    private String photoUrl;
    private int continueHour;
    private int likeCnt;
    private String deleteTime;
    private long id;
    private int photoFeed;
    private String videourlKs;
    private String feid;
    private String fid;
    private int durationSeconds;
    private  String sendTime;
    private int spiderdeleteCnt;
    private int playCnt;
    private String createTime;
    private long kid;
    private int videoType;
    private String videoUrl;
    private String keid;
    private int deletedFeed;
    private int commentCnt;

    //客户端0为未打标签
    private int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getContinueHour() {
        return continueHour;
    }

    public void setContinueHour(int continueHour) {
        this.continueHour = continueHour;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPhotoFeed() {
        return photoFeed;
    }

    public void setPhotoFeed(int photoFeed) {
        this.photoFeed = photoFeed;
    }

    public String getVideourlKs() {
        return videourlKs;
    }

    public void setVideourlKs(String videourlKs) {
        this.videourlKs = videourlKs;
    }

    public String getFeid() {
        return feid;
    }

    public void setFeid(String feid) {
        this.feid = feid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getSpiderdeleteCnt() {
        return spiderdeleteCnt;
    }

    public void setSpiderdeleteCnt(int spiderdeleteCnt) {
        this.spiderdeleteCnt = spiderdeleteCnt;
    }

    public int getPlayCnt() {
        return playCnt;
    }

    public void setPlayCnt(int playCnt) {
        this.playCnt = playCnt;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getKid() {
        return kid;
    }

    public void setKid(long kid) {
        this.kid = kid;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getKeid() {
        return keid;
    }

    public void setKeid(String keid) {
        this.keid = keid;
    }

    public int getDeletedFeed() {
        return deletedFeed;
    }

    public void setDeletedFeed(int deletedFeed) {
        this.deletedFeed = deletedFeed;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

}
