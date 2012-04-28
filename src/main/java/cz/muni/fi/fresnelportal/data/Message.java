/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.fresnelportal.data;

/**
 *
 * @author nodrock
 */
public class Message {
    public final static String SUCCESS = "success";
    public final static String ERROR = "error";
    public final static String NOTICE = "notice";
    
    private String type;
    private String text;
    private String desc;

    public Message() {
    }

    public Message(String type, String text, String desc) {
        this.type = type;
        this.text = text;
        this.desc = desc;
    }
    
    public Message(String type, String text) {
        this.type = type;
        this.text = text;
        this.desc = "";
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
