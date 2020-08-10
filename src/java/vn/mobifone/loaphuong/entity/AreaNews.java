/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.mobifone.loaphuong.lib.SystemConfig;

/**
 *
 * @author cuong.trinh
 */
public class AreaNews implements Serializable{
    private String title_en;
    private String area_name;
    private long area_id;
    private long area_type_id;
    private int notify_flag;
    private String publish_date;
    private int status;
    private int is_active;
    private long num_of_views;
    private String full_description;
    private long category_id;
    private String image;
    private String urlImage;
    private String short_description_en;
    private String from_date;
    private String url;
    private String fullUrl;
    private int notify_status;
    private long id;
    private long num_of_likes;
    private String title;
    private String description_en;
    private String updated_at;
    private String short_description;
    private String create_date;
    private long create_user;
    private String to_date;
    private int is_sticky;
    private long num_of_comments;
    
    private String categoryName;
    private Date fromDate;
    private Date toDate;
    private Boolean notifyFlagBool;
    private Boolean isStickyBool;
    private String imageName;
    private Date createDate;
    
    

    SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy-MM-dd");    //2017-07-31 06:38:45.867
    SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");    //2017-07-31 06:38:45.867
    SimpleDateFormat DateFormatterFake = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat DateFormatterFake1 = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat DateFormatterFake2 = new SimpleDateFormat("HH:mm:ss");
    public AreaNews() {
    }

    public Date getFromDate() {
        try {
            if (publish_date != null) {
                fromDate = DateFormatter2.parse(publish_date);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
        try {
           publish_date = DateFormatter2.format(fromDate);

        } catch (Exception e) {
            
        }
    }

    public Date getToDate() {
        try {
            if (to_date != null) {
                toDate = DateFormatter2.parse(to_date);
            }
        } catch (ParseException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
        try {
           to_date = DateFormatter2.format(toDate);

        } catch (Exception e) {
            
        }
    }
    
    
    
    
    

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    
    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public long getArea_id() {
        return area_id;
    }

    public void setArea_id(long area_id) {
        this.area_id = area_id;
    }

    public int getNotify_flag() {
        return notify_flag;
    }

    public void setNotify_flag(int notify_flag) {
        this.notify_flag = notify_flag;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public long getNum_of_views() {
        return num_of_views;
    }

    public void setNum_of_views(long num_of_views) {
        this.num_of_views = num_of_views;
    }

    public String getFull_description() {
        return full_description;
    }

    public void setFull_description(String full_description) {
        this.full_description = full_description;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        imageName = image;
    }
    public String getUrlImage() {
        return SystemConfig.getConfig("ReadAsset")+ image;
    }

    public void setUrlImage(String image) {
        this.urlImage = urlImage;
    }

    public String getShort_description_en() {
        return short_description_en;
    }

    public void setShort_description_en(String short_description_en) {
        this.short_description_en = short_description_en;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getUrl() {
        return url;
    }
    public String getFullUrl() {
        if (url != null)
            fullUrl = SystemConfig.getConfig("ReadAsset") + url;
        else
            fullUrl = "";
        return fullUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setFullUrl(String url) {
        this.fullUrl = url;
    }

    public int getNotify_status() {
        return notify_status;
    }

    public void setNotify_status(int notify_status) {
        this.notify_status = notify_status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNum_of_likes() {
        return num_of_likes;
    }

    public void setNum_of_likes(long num_of_likes) {
        this.num_of_likes = num_of_likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public long getCreate_user() {
        return create_user;
    }

    public void setCreate_user(long create_user) {
        this.create_user = create_user;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public int getIs_sticky() {
        return is_sticky;
    }

    public void setIs_sticky(int is_sticky) {
        this.is_sticky = is_sticky;
    }

    public long getNum_of_comments() {
        return num_of_comments;
    }

    public void setNum_of_comments(long num_of_comments) {
        this.num_of_comments = num_of_comments;
    }

    public SimpleDateFormat getDateFormatter() {
        return DateFormatter;
    }

    public void setDateFormatter(SimpleDateFormat DateFormatter) {
        this.DateFormatter = DateFormatter;
    }

    public Boolean getNotifyFlagBool() {
        return notify_flag ==1;
    }

    public void setNotifyFlagBool(Boolean notifyFlagBool) {
        this.notifyFlagBool = notifyFlagBool;
        this.notify_flag = notifyFlagBool?1:0;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Boolean getIsStickyBool() {
        return is_sticky == 1;
    }

    public void setIsStickyBool(Boolean isStickyBool) {
        this.isStickyBool = isStickyBool;
        this.is_sticky = isStickyBool?1:0;
    }

    public Date getCreateDate() {
        try {
            createDate = DateFormatter2.parse(create_date);
        } catch (ParseException ex) {
            Logger.getLogger(AreaNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getArea_type_id() {
        return area_type_id;
    }

    public void setArea_type_id(long area_type_id) {
        this.area_type_id = area_type_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
    

    


    
    
    
}
