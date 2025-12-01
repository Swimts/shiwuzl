package com.swzl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;


/**
 *
 * @TableName lostandfound
 */
@TableName(value ="lostandfound")
@Data
public class Lostandfound implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    @TableField(value = "userid")
    private Integer userid;

    /**
     *
     */
    @TableField(value = "lostorfound")
    private Integer lostorfound;

    /**
     *
     */
    @TableField(value = "itemname")
    private String itemname;

    /**
     *
     */
    @TableField(value = "description")
    private String description;

    /**
     *
     */
    @TableField(value = "location")
    private String location;

    /**
     *
     */
    @TableField(value = "imagepath")
    private String imagepath;

    /**
     *
     */
    @TableField(value = "claimed")
    private Integer claimed;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;



    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Lostandfound other = (Lostandfound) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getLostorfound() == null ? other.getLostorfound() == null : this.getLostorfound().equals(other.getLostorfound()))
            && (this.getItemname() == null ? other.getItemname() == null : this.getItemname().equals(other.getItemname()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getLocation() == null ? other.getLocation() == null : this.getLocation().equals(other.getLocation()))
            && (this.getImagepath() == null ? other.getImagepath() == null : this.getImagepath().equals(other.getImagepath()))
            && (this.getClaimed() == null ? other.getClaimed() == null : this.getClaimed().equals(other.getClaimed()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getLostorfound() == null) ? 0 : getLostorfound().hashCode());
        result = prime * result + ((getItemname() == null) ? 0 : getItemname().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        result = prime * result + ((getImagepath() == null) ? 0 : getImagepath().hashCode());
        result = prime * result + ((getClaimed() == null) ? 0 : getClaimed().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", lostorfound=").append(lostorfound);
        sb.append(", itemname=").append(itemname);
        sb.append(", description=").append(description);
        sb.append(", location=").append(location);
        sb.append(", imagepath=").append(imagepath);
        sb.append(", claimed=").append(claimed);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }


    public void setImagePath(String imagePath) {
        this.imagepath = imagePath;
    }



}