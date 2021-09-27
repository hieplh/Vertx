package com.example.hiep.error;

import java.io.Serializable;

public class Error implements Serializable {

    private String attrId;
    private String attrClass;
    private String attrName;
    private String errMsg;

    public Error() {
    }

    public Error(String attrId, String attrClass, String attrName, String errMsg) {
        this.attrId = attrId;
        this.attrClass = attrClass;
        this.attrName = attrName;
        this.errMsg = errMsg;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getAttrClass() {
        return attrClass;
    }

    public void setAttrClass(String attrClass) {
        this.attrClass = attrClass;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "Error{" + "attrId=" + attrId + ", attrClass=" + attrClass + ", attrName=" + attrName + ", errMsg=" + errMsg + '}';
    }

}
